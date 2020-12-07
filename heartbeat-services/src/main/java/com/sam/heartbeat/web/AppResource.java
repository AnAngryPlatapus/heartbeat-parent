package com.sam.heartbeat.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.service.HeartbeatAppService;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppResource {

    private final HeartbeatAppService heartbeatAppService;

    @GetMapping("/all")
    private Flux<HeartbeatApp> findAllApps() {
        return heartbeatAppService.findAllApps();
    }

    @PostMapping("/init")
    private Mono<HeartbeatApp> initApp(@RequestBody HeartbeatApp app) {
        return heartbeatAppService.onAppInit(app);
    }


}
