package com.sam.heartbeat.web.socket;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.slack.SocketMessage;
import com.sam.heartbeat.model.type.Status;
import com.sam.heartbeat.service.HeartbeatAppService;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackSocketHandler implements WebSocketHandler {

    private final HeartbeatAppService heartbeatAppService;

    private Consumer<WebSocketMessage> printingConsumer =
            webSocketMessage -> System.out.println(webSocketMessage.getPayloadAsText());

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.send(heartbeatAppService.findAndWatchByStatus(Status.DOWN)
                .map(SocketMessage::new)
                .map(ssm -> session.textMessage(ssm.toJson())))
                .and(session.receive().doOnNext(printingConsumer));
    }

}
