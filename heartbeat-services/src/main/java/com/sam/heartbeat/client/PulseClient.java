package com.sam.heartbeat.client;

import static java.util.function.Function.identity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.Pulse;
import com.sam.heartbeat.model.type.Status;

@Slf4j
@Service
@RequiredArgsConstructor
public class PulseClient {


    @Value("${app.buffer.size:4096}")
    private Integer bufferSize;
    private final DefaultDataBufferFactory defaultDataBufferFactory;

    private final WebClient webClient;

    private String actuatorUri(final HeartbeatApp app) {
        return UriComponentsBuilder.fromHttpUrl(app.getHostname())
                .pathSegment(app.getContext())
                .pathSegment("actuator").toUriString();
    }

    private List<String> getLinesAsList(String string) {
        Supplier<Stream<String>> streamSupplier = string::lines;
        return streamSupplier.get().collect(Collectors.toList());
    }

    public Flux<DataBuffer> pulseRawLogs(final String path) {
        log.info("GET At: {}", path);
        return webClient.get().uri(path).exchange()
                .flatMapMany(clientResponse ->
                        clientResponse.body(BodyExtractors.toDataBuffers()));
    }

    public Flux<String> logsFlux(String filePath, Integer limit) {
        return pulseRawLogs(filePath)
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                }).map(this::getLinesAsList)
                .flatMapIterable(identity())
                .takeLast(limit);
    }

    public Mono<String> pulseRecentLogs(String filePath, Integer limit) {
        return pulseRawLogs(filePath)
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return new String(bytes, StandardCharsets.UTF_8);
                }).map(this::getLinesAsList)
                .flatMapIterable(identity())
                .takeLast(limit)
                .reduce((a, b) -> a.concat(b + "\n"));
    }

    public Mono<Pulse> pulseHeartApp(final HeartbeatApp app) {
        var uri = actuatorUri(app);
        var start = System.currentTimeMillis();
        log.trace("GET At: {} ", uri);
        return webClient.get().uri(uri).exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Object.class))
                .flatMap(actuatorObject -> {
                    final var end = System.currentTimeMillis();
                    return Mono.just(new Pulse(
                            actuatorObject,
                            LocalDateTime.now(),
                            end - start,
                            app.getStatus() != Status.UP,
                            app.toBuilder().status(Status.UP).build()));
                })
                .onErrorReturn(err -> {
                    log.trace("App: {} Threw: {}", app.getName(), err.getMessage());
                    return true;
                }, Pulse.DEFAULT_DOWN(app.toBuilder().status(Status.DOWN).build(),
                        app.getStatus() != Status.DOWN));
    }

}