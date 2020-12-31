package com.sam.heartbeat.web;

import java.net.URI;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.slack.AuthCode;
import com.sam.heartbeat.model.slack.OAuthToken;
import com.sam.heartbeat.repository.AuthCodeRepository;
import com.sam.heartbeat.service.SlackService;
import com.sam.heartbeat.web.socket.SlackSocketHandler;

@Slf4j
@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackResource {

    private final SlackService slackService;
    private final SlackSocketHandler slackSocketHandler;
    private final AuthCodeRepository authCodeRepository;

    @GetMapping("/auth")
    public Flux<AuthCode> authCodes() {
        return authCodeRepository.findAll();
    }

    @GetMapping("/auth/redirect")
    public Mono<AuthCode> authRedirect(@RequestParam String code) {
        log.info("authCode {}", code);
        return authCodeRepository.save(new AuthCode(LocalDateTime.now(), code));
    }

    @GetMapping("/auth/token")
    public Mono<OAuthToken> getAccessToken() {
        return slackService.getAccessToken();
    }

    @PostMapping("/socket")
    public void initWebsocketConnection(@RequestParam String uri) {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create(uri), slackSocketHandler).subscribe();
    }




}

