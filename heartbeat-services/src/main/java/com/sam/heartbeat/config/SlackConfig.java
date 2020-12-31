package com.sam.heartbeat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SlackConfig {

    @Value("${slack.access.token}")
    private String bearerToken;

    @Bean
    public WebClient authSlackClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", bearerToken))
                .build();
    }

    @Bean
    public WebSocketClient slackSocketClient() {
        return new ReactorNettyWebSocketClient();
    }

}
