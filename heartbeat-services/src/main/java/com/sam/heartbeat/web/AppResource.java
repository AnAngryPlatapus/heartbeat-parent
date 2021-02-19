package com.sam.heartbeat.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.slack.ActionBody;
import com.sam.heartbeat.model.slack.ActionMessage;
import com.sam.heartbeat.service.HeartbeatAppService;
import com.sam.heartbeat.service.SlackService;

@Slf4j
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppResource {

    private final ObjectMapper objectMapper;
    private final SlackService slackService;
    private final HeartbeatAppService heartbeatAppService;

    @GetMapping("/all")
    private Flux<HeartbeatApp> findAllApps() {
        return heartbeatAppService.findAllApps();
    }

    @PostMapping("/init")
    private Mono<HeartbeatApp> initApp(@RequestBody HeartbeatApp app) {
        return heartbeatAppService.onAppInit(app);
    }

    @PostMapping(value = "/hook/action")
    public Mono<Void> getApplicationLogs(@ModelAttribute Mono<ActionMessage> message) {
        return message.flatMap(m -> Mono.fromCallable(() -> objectMapper.readValue(m.getPayload(), ActionBody.class)))
                .flatMap(heartbeatAppService::actionHandler);
    }

    @PostMapping("/test")
    public Mono<Object> test() {
        return heartbeatAppService.findByName("Demo Application")
                .flatMap(slackService::statusMessage);
    }
}
