package com.sam.heartbeat.service;

import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.client.PulseClient;
import com.sam.heartbeat.model.Action;
import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.slack.ActionBody;
import com.sam.heartbeat.model.slack.Block;
import com.sam.heartbeat.model.slack.BlockMessage;
import com.sam.heartbeat.model.slack.BlockType;
import com.sam.heartbeat.model.slack.Channel;
import com.sam.heartbeat.model.slack.Member;
import com.sam.heartbeat.model.slack.Text;
import com.sam.heartbeat.model.type.Status;
import com.sam.heartbeat.repository.HeartbeatAppRepository;
import com.sam.heartbeat.service.job.HeartbeatJobUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartbeatAppService {

    @Value("${config.log.file.limit:100}")
    private Integer logLineLimit;

    private final Scheduler scheduler;
    private final PulseClient pulseClient;
    private final SlackService slackService;
    private final HeartbeatJobUtil heartbeatJobUtil;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final HeartbeatAppRepository heartbeatAppRepository;

    @PostConstruct
    public void init() {
//            watchAppsByStatus(Status.DOWN)
//                    .flatMap(slackService::sendComponentMessage)
//                    .subscribe();
    }

    public Mono<HeartbeatApp> findByName(String name) {
        return heartbeatAppRepository.findByName(name);
    }

    //TODO refactor so there is one save call and after save you start pulse
    public Mono<HeartbeatApp> onAppInit(HeartbeatApp heartbeatApp) {
        return heartbeatAppRepository.findByName(heartbeatApp.getName())
                .switchIfEmpty(Mono.defer(() -> startPulse(heartbeatAppRepository.save(heartbeatApp))))
                .flatMap(app -> {
                    app.setStatus(Status.UP);
                    return heartbeatAppRepository.save(app);
                }).filter(app -> !app.isSlackConfigured())
                .flatMap(app -> Mono.zip(slackService.getChannelMap(), slackService.getMemberMap(), Mono.just(app)))
                .flatMap(cma -> {
                    HeartbeatApp app = cma.getT3();
                    Channel channel = cma.getT1().get(app.getChannelName());
                    Member member = cma.getT2().get(app.getBotName());
                    return app.configureSlack(channel, member);
                }).flatMap(heartbeatAppRepository::save);
    }

    private Mono<HeartbeatApp> startPulse(Mono<HeartbeatApp> heartbeatAppMono) {
        return heartbeatAppMono.map(heartbeatApp -> {
            var heartbeatJobDetail = heartbeatJobUtil.buildJobDetail(heartbeatApp);
            try {
                scheduler.scheduleJob(heartbeatJobDetail, heartbeatJobUtil.buildTrigger(heartbeatJobDetail));
            } catch (SchedulerException se) {
                log.error("startPulse: {}", se.getMessage());
            }
            return heartbeatApp;
        });
    }

    public Flux<HeartbeatApp> watchAppsByStatus(Status status) {
        var statusFilter = Aggregation.newAggregation(Aggregation.match(Criteria.where("status").is(status.name())));
        var options = ChangeStreamOptions.builder()
                .returnFullDocumentOnUpdate()
                .filter(statusFilter)
                .build();

        return reactiveMongoTemplate.changeStream("heartbeatApp", options, HeartbeatApp.class)
                .map(heartbeatAppChangeStreamEvent ->
                        Objects.requireNonNull(heartbeatAppChangeStreamEvent.getBody()));
    }

    public Flux<HeartbeatApp> watchApps() {
        var options = ChangeStreamOptions.builder()
                .returnFullDocumentOnUpdate()
                .build();
        return reactiveMongoTemplate.changeStream("heartbeatApp", options, HeartbeatApp.class)
                .map(heartbeatAppChangeStreamEvent ->
                        Objects.requireNonNull(heartbeatAppChangeStreamEvent.getBody()));
    }

    //TODO
    public Mono<Void> actionHandler(ActionBody actionBody) {
        return Flux.fromIterable(actionBody.getActions())
                .map(Block::getValue)
                .next()
                .flatMap(this::handleHelper);
    }

    // TODO refactor to your standards
    private Mono<Void> handleHelper(String value) {
        var values = value.split(":");
        var name = values[0];
        var action = Action.of(values[1]);

        switch (action) {
            case LOGS:
                return findByName(name)
                        .flatMap(app -> buildLogMessage(app, pulseClient.pulseRecentLogs(app.getLogPath(), 20)))
                        .flatMap(slackService::sendBlockMessage)
                        .then();

            case SCRIPTS:
            case CASE:
            default:
                return null;
        }
    }

    private Mono<BlockMessage> buildLogMessage(HeartbeatApp app, Mono<String> appLogs) {
        return appLogs.map(logs -> BlockMessage.builder()
                .channel(app.getChannelId())
                .blocks(
                        List.of(
                             Block.builder()
                                     .type(BlockType.SECTION)
                                     .text(Text.builder()
                                             .type(BlockType.MARKDOWN)
                                             .text(logs)
                                             .build())
                                     .build())).build());
    }

    public Mono<HeartbeatApp> findById(String id) {
        return heartbeatAppRepository.findById(id);
    }

    public Mono<HeartbeatApp> save(HeartbeatApp app) {
        return heartbeatAppRepository.save(app);
    }


    public Flux<HeartbeatApp> findAllApps() {
        return heartbeatAppRepository.findAll();
    }

    public void deleteAppApps() {
        heartbeatAppRepository.deleteAll();
    }


}