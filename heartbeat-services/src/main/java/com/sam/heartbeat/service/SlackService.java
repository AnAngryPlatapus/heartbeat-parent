package com.sam.heartbeat.service;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.client.SlackClient;
import com.sam.heartbeat.model.slack.Channel;
import com.sam.heartbeat.model.slack.Conversation;
import com.sam.heartbeat.model.slack.OAuthToken;
import com.sam.heartbeat.model.slack.RtmConnect;
import com.sam.heartbeat.repository.AuthCodeRepository;
import com.sam.heartbeat.repository.OAuthTokenRepository;
import com.sam.heartbeat.web.socket.SlackSocketHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {

    private final SlackClient slackClient;
    private final WebSocketClient slackSocketClient;
    private final SlackSocketHandler slackSocketHandler;
    private final AuthCodeRepository authCodeRepository;
    private final OAuthTokenRepository oAuthTokenRepository;


    @PostConstruct
    public void init() {
        initWebSocketConnection(slackClient.getRtmConnection().map(RtmConnect::getUrl));
    }

    private void initWebSocketConnection(Mono<String> uri) {
        uri.flatMap(socketUri -> slackSocketClient.execute(URI.create(socketUri), slackSocketHandler))
                .switchIfEmpty(Mono.error(new IllegalStateException("Error connecting to socket")))
                .subscribe();
    }

    public Mono<OAuthToken> getAccessToken() {
        return authCodeRepository.findTopByOrderByCreatedAtDesc()
                .flatMap(slackClient::getAccessToken)
                .flatMap(oAuthTokenRepository::save);
    }

    //TODO up for debate how I did this
    public Mono<Map<String, Channel>> getChannelMap() {
        return slackClient.getChannels().map(Conversation::getChannels)
                .flatMap(channels -> Flux.fromIterable(channels).collectMap(Channel::getName, Function.identity()));
    }

}
