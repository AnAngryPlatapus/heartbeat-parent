package com.sam.heartbeat.client;

import java.time.LocalDateTime;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.slack.AuthCode;
import com.sam.heartbeat.model.slack.BlockMessage;
import com.sam.heartbeat.model.slack.Conversation;
import com.sam.heartbeat.model.slack.OAuthToken;
import com.sam.heartbeat.model.slack.RtmConnect;
import com.sam.heartbeat.model.slack.Users;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackClient {


    @Value("${slack.hostname}")
    private String hostname;
    @Value("${heartbeat.callback.url}")
    private String callbackUrl;
    @Value("${slack.client.id}")
    private String clientId;
    @Value("${slack.client.secret}")
    private String clientSecret;
    @Value("${slack.access.token}")
    private String bearerToken;
    @Value("${slack.auth.scopes}")
    private String scopes;
    @Value("${slack.web.hook}")
    private String webHook;

    private final WebClient authSlackClient;
    private final WebClient socketClient;

    public Mono<LocalDateTime> initAuth() {
        final var uri = UriComponentsBuilder.fromHttpUrl(hostname).pathSegment("oauth", "authorize")
                .queryParam("client_id", clientId)
                .queryParam("scope", scopes)
                .queryParam("redirect_uri", callbackUrl)
                .toUriString();
        log.info("GET at: {}", uri);
        return WebClient.create(uri).get().exchange().flatMap(clientResponse -> Mono.just(LocalDateTime.now()));
    }

    public Mono<OAuthToken> getAccessToken(AuthCode authCode) {
        final var uri = UriComponentsBuilder.fromHttpUrl(hostname).pathSegment("api", "oauth.access").toUriString();
        log.info("POST at: {}", uri);
        return WebClient.builder().baseUrl(uri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build().post().body(
                        BodyInserters.fromFormData("client_id", clientId)
                                .with("client_secret", clientSecret)
                                .with("code", authCode.getCode())
                                .with("redirect_uri", callbackUrl)
                ).exchange().flatMap(clientResponse -> clientResponse.bodyToMono(OAuthToken.class));
    }

    public Mono<RtmConnect> getRtmConnection() {
        final var uri = UriComponentsBuilder.fromHttpUrl(hostname).pathSegment("api", "rtm.connect").toUriString();
        log.info("GET at: {}", uri);
        return authSlackClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(RtmConnect.class);
    }

    public Mono<RtmConnect> getSocketConnection() {
        final var uri = UriComponentsBuilder.fromHttpUrl(hostname).pathSegment("api", "apps.connections.open").toUriString();
        log.info("POST at: {}", uri);
        return socketClient.post()
                .uri(uri)
                .retrieve()
                .bodyToMono(RtmConnect.class);
    }


    public Mono<Conversation> getChannels() {
        final var uri = UriComponentsBuilder.fromHttpUrl(hostname).pathSegment("api", "conversations.list").toUriString();
        log.info("GET at: {}", uri);
        return authSlackClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Conversation.class);
    }

    public Mono<Users> getMembers() {
        final var uri = UriComponentsBuilder.fromHttpUrl(hostname).pathSegment("api", "users.list").toUriString();
        log.info("GET at: {}", uri);
        return authSlackClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Users.class);
    }


    public Mono<Object> blockMessage(Payload payload) {
        final var uri = UriComponentsBuilder.fromHttpUrl(webHook).toUriString();
        log.info("POST at: {}", uri);
        return authSlackClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(payload))
                .exchange()
                .map(clientResponse -> clientResponse.bodyToMono(Object.class));
    }

    public Mono<Void> sendBlockMessage(BlockMessage component) {
        final var uri = UriComponentsBuilder.fromHttpUrl(webHook).toUriString();
        log.info("POST at: {} with: {}", uri, component.toJson());
        return authSlackClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(component))
                .exchange()
                .then();
    }
}