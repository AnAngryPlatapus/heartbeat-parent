package com.sam.heartbeat.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestConfig {

    @Value("${slack.access.token}")
    private String bearerToken;

    @Value("${slack.socket.token}")
    private String socketToken;


    @Bean
    public WebClient authSlackClient() {
        var encoder = new Jackson2JsonEncoder(webclientMapper(), MediaType.APPLICATION_JSON);
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
                                .jackson2JsonEncoder(encoder))
                        .build())
                .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", bearerToken))
                .build();
    }

    @Bean
    public WebClient socketClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", socketToken))
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public WebSocketClient slackSocketClient() {
        return new ReactorNettyWebSocketClient();
    }


    @Bean
    public ObjectMapper webclientMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

}
