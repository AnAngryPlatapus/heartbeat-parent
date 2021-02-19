package com.sam.heartbeat.web;

import java.io.IOException;
import java.net.URI;
import javax.annotation.PostConstruct;
import com.slack.api.bolt.App;
import com.slack.api.bolt.socket_mode.SocketModeApp;
import com.slack.api.methods.SlackApiException;
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
import com.sam.heartbeat.service.SlackService;
import com.sam.heartbeat.web.socket.SlackSocketHandler;

@Slf4j
@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackResource {

    private final App slackApp;
    private final SlackService slackService;
    private final SlackSocketHandler slackSocketHandler;

    @PostConstruct
    void init() throws Exception {
        slackService.createWebSocketConnection(slackSocketHandler);
        new SocketModeApp(slackApp).startAsync();
    }

    @GetMapping("/auth")
    public Flux<AuthCode> authCodes() {
        return slackService.findAllAuthCodes();
    }

    @GetMapping("/auth/redirect")
    public Mono<AuthCode> authRedirect(@RequestParam String code) {
        return slackService.redirect(code);
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

    @PostMapping
    public void sendOverSocket() throws IOException, SlackApiException {
        System.out.println(System.getenv("SLACK_APP_TOKEN"));

//        client.sendWebSocketMessage(
//                "{\"type\":\"message\"," +
//                        "\"channel\":\"C01J7DN46Q1\"," +
//                        "\"user\":\"U01H4QFA3M0\"," +
//                        "\"text\":\"Demo Application is Down\"," +
//                        "\"ts\":\"1612046864\"}");
//        client.connectToNewEndpoint();
//        System.out.println(client.verifyConnection());
        System.out.println("sending message");
    }


}

