package com.sam.heartbeat.web;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.client.PulseClient;

@Slf4j
@RestController
@RequestMapping("/pulse")
@RequiredArgsConstructor
public class PulseResource {

    private final PulseClient pulseClient;

    private List<String> processAndGetLinesAsList(String string) {
        Supplier<Stream<String>> streamSupplier = string::lines;
        return streamSupplier.get().collect(toList());
    }

    @GetMapping("/file")
    public Mono<String> getLogOutput(@RequestParam String filePath, @RequestParam Integer limit) {
        return pulseClient.pulseRecentLogs(filePath, limit);
    }


}
