package com.sam.heartbeat.web.socket;

import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.service.HeartbeatAppService;
import com.sam.heartbeat.service.SlackService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackSocketHandler implements WebSocketHandler {

    private final SlackService slackService;
    private final HeartbeatAppService heartbeatAppService;
    private final Consumer<WebSocketMessage> loggingConsumer =
            webSocketMessage -> log.info(webSocketMessage.getPayloadAsText());

    @PostConstruct
    public void init() {
        heartbeatAppService.watchApps()
                .flatMap(slackService::statusMessage)
                .subscribe();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive().doOnNext(loggingConsumer).then();
    }

}
