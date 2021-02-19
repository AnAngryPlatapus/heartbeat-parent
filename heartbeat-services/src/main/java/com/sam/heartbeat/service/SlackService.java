package com.sam.heartbeat.service;

import static java.lang.String.format;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.client.PulseClient;
import com.sam.heartbeat.client.SlackClient;
import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.slack.AuthCode;
import com.sam.heartbeat.model.slack.BlockMessage;
import com.sam.heartbeat.model.slack.Channel;
import com.sam.heartbeat.model.slack.Conversation;
import com.sam.heartbeat.model.slack.Member;
import com.sam.heartbeat.model.slack.OAuthToken;
import com.sam.heartbeat.model.slack.RtmConnect;
import com.sam.heartbeat.model.slack.Users;
import com.sam.heartbeat.repository.AuthCodeRepository;
import com.sam.heartbeat.repository.OAuthTokenRepository;
import com.sam.heartbeat.web.socket.SlackSocketHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {

    private final SlackClient slackClient;
    private final PulseClient pulseClient;
    private final WebSocketClient slackSocketClient;
    private final AuthCodeRepository authCodeRepository;
    private final OAuthTokenRepository oAuthTokenRepository;

    @Value("${slack.component.test}")
    private Resource testComponent;

    @PostConstruct
    public void init() {
//        initAuth();
    }

    private void initAuth() {
        //TODO fix this
        slackClient.initAuth();
    }

    public void createWebSocketConnection(SlackSocketHandler slackSocketHandler) {
        slackClient.getRtmConnection()
                .map(RtmConnect::getUrl)
                .flatMap(uri -> slackSocketClient.execute(URI.create(uri), slackSocketHandler))
                .switchIfEmpty(Mono.error(new IllegalStateException("Error connecting to socket")))
                .subscribe();
    }

    public void createSocketModeConnection(SlackSocketHandler slackSocketHandler) {
        slackClient.getSocketConnection()
                .map(RtmConnect::getUrl)
                .flatMap(uri -> slackSocketClient.execute(URI.create(uri), slackSocketHandler))
                .switchIfEmpty(Mono.error(new IllegalStateException("Error connecting to socket")))
                .subscribe();
    }

    //TODO up for debate how I did this, also should cache
    public Mono<Map<String, Channel>> getChannelMap() {
        return slackClient.getChannels().map(Conversation::getChannels)
                .flatMap(channels -> Flux.fromIterable(channels).collectMap(Channel::getName, Function.identity()));
    }

    public Mono<Map<String, Member>> getMemberMap() {
        return slackClient.getMembers().map(Users::getMembers)
                .flatMap(members -> Flux.fromIterable(members).collectMap(Member::getName, Function.identity()));
    }

    public Mono<OAuthToken> getAccessToken() {
        return authCodeRepository.findTopByOrderByCreatedAtDesc()
                .flatMap(slackClient::getAccessToken)
                .flatMap(oAuthTokenRepository::save);
    }

    public Mono<AuthCode> redirect(String code) {
        log.info("authCode {}", code);
        return authCodeRepository.save(new AuthCode(LocalDateTime.now(), code));
    }

    public Flux<AuthCode> findAllAuthCodes() {
        return authCodeRepository.findAll();
    }

    //TODO refactor to get commands list dynamically
    public Mono<Object> statusMessage(HeartbeatApp app) {
        var message = format("Application %s is %s", app.getName(), app.getStatus().getDisplayName());
        return slackClient.blockMessage(Payload.builder()
                .text(message)
                .blocks(List.of(
                        HeaderBlock.builder()
                                .text(PlainTextObject.builder()
                                        .text(message)
                                        .build())
                                .build(),
                        new DividerBlock(),
                        SectionBlock.builder()
                                .text(MarkdownTextObject.builder()
                                        .text("Available commands are: /logs /scripts /cases")
                                        .build())
                                .build(),
                        SectionBlock.builder()
                                .text(MarkdownTextObject.builder()
                                        .text("Trying running commands in the form: \n /command app-name [optional] opts..")
                                        .build())
                                .build()
                )).build());
    }

    public Mono<Void> sendBlockMessage(BlockMessage message) {
        return slackClient.sendBlockMessage(message);
    }

}
