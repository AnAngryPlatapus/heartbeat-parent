package com.sam.heartbeat.client;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.Pulse;
import com.sam.heartbeat.model.type.Status;
import com.sam.heartbeat.repository.HeartbeatAppRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PulseClient {

    private final HeartbeatAppRepository heartbeatAppRepository;

    private String uri(HeartbeatApp app) {
        return UriComponentsBuilder.fromHttpUrl(app.getHostname())
                .pathSegment(app.getContext())
                .pathSegment("actuator").toUriString();
    }

    public Mono<Pulse> pulseHeartbeatApp(HeartbeatApp app) {
        Long start = System.currentTimeMillis();
        log.info(uri(app));
        return WebClient.create(uri(app)).get().exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Object.class))
                .flatMap(actuatorObject -> {
                    Long end = System.currentTimeMillis();
                    return Mono.just(new Pulse(actuatorObject,
                            LocalDateTime.now(),
                            end - start,
                            app));
                })
                .onErrorReturn(err -> {
                    log.error("App: {} Threw: {}", app.getName(), err.getMessage());
                    app.setStatus(Status.DOWN);
                    heartbeatAppRepository.save(app).subscribe();
                    return true;
                }, Pulse.DEFAULT(app));
    }
}
