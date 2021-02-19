package com.sam.heartbeat.config;

import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

import com.slack.api.bolt.App;
import com.slack.api.bolt.context.builtin.SlashCommandContext;
import com.slack.api.bolt.request.builtin.SlashCommandRequest;
import com.slack.api.bolt.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import com.sam.heartbeat.client.PulseClient;
import com.sam.heartbeat.service.HeartbeatAppService;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SlackConfig {

    @Value("${chat.log.limit:20}")
    private int logLimit;

    private final PulseClient pulseClient;
    private final HeartbeatAppService heartbeatAppService;

    @Bean
    public App slackApp() {
        return new App()
                .command("/hello", (req, ctx) -> ctx.ack("Sup dog"))
                .command("/logs", this::handleLogs)
                .command("/scripts", this::handleScripts)
                .command("/cases", this::handleCases);
    }

    //TODO throw slack exception;
    private Response handleLogs(SlashCommandRequest req, SlashCommandContext ctx) {
        var attributes = Flux.fromArray(req.getPayload().getText().split(" "));
        var appName = attributes.blockFirst();

        var heartbeatApp = heartbeatAppService.findByName(appName)
                .switchIfEmpty(error(RuntimeException::new));

        var logs = heartbeatApp.flatMapMany(app -> pulseClient.logsFlux(app.getLogPath(), logLimit));

        return heartbeatApp
                .flatMap(app -> app.logsBlock(logs.reduce((a, b) -> a.concat(format("%s\n", b)))))
                .map(ctx::ack)
                .block();
    }

    private Response handleScripts(SlashCommandRequest req, SlashCommandContext ctx) {
        var attributes = Flux.fromArray(req.getPayload().getText().split(" "));
        var appName = attributes.blockFirst();

        var heartbeatApp = heartbeatAppService.findByName(appName)
                .switchIfEmpty(error(RuntimeException::new));

        return null;
    }

    private Response handleCases(SlashCommandRequest req, SlashCommandContext ctx) {
        var attributes = Flux.fromArray(req.getPayload().getText().split(" "));
        var appName = attributes.blockFirst();

        var heartbeatApp = heartbeatAppService.findByName(appName)
                .switchIfEmpty(error(RuntimeException::new));

        //TODO
       throw new RuntimeException("TODO IMPLEMENT");
    }

}
