package com.sam.heartbeat.service;

import java.util.Objects;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.model.HeartbeatApp;
import com.sam.heartbeat.model.type.Status;
import com.sam.heartbeat.repository.HeartbeatAppRepository;
import com.sam.heartbeat.service.job.HeartbeatJobUtil;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartbeatAppService {

    private final Scheduler scheduler;
    private final SlackService slackService;
    private final HeartbeatJobUtil heartbeatJobUtil;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final HeartbeatAppRepository heartbeatAppRepository;


    @PostConstruct
    public void init() {
        int i = 0;
//        Flux.zip(slackService.getChannelMap(), findAllApps(),
//                (channels, app) -> {
//                    if(!app.isSlackConfigured()) {
//                        return save(app.congig)
//                    }
//                    System.out.println(String.format("Run i %i", i++));
//                });

    }

//    public Mono<HeartbeatApp> onAppInit(HeartbeatApp heartbeatApp) {
//       Mono<Map<String, Channel>> channels = slackService.getChannelMap();
//        findAllApps().flatMap(app -> )
//        if (heartbeatApp.isSlackConfigured()) {
//
//        }
//        //TODO save to db
//        Map<String, >
//
//        return heartbeatAppRepository.findByName(heartbeatApp.getName())
//                .switchIfEmpty(Mono.defer(() -> startPulse(heartbeatAppRepository.save(heartbeatApp))))
//                .flatMap(app -> {
//                    app.setStatus(Status.UP);
//                    return heartbeatAppRepository.save(app);
//                });
//    }

    private Mono<HeartbeatApp> startPulse(Mono<HeartbeatApp> heartbeatAppMono) {
        return heartbeatAppMono.map(heartbeatApp -> {
            JobDetail heartbeatJobDetail = heartbeatJobUtil.buildJobDetail(heartbeatApp);
            try {
                scheduler.scheduleJob(heartbeatJobDetail, heartbeatJobUtil.buildTrigger(heartbeatJobDetail));
            } catch (SchedulerException se) {
                log.error("startPulse: {}", se.getMessage());
            }
            return heartbeatApp;
        });
    }

    public Flux<HeartbeatApp> findAllByStatusTrail(Status status) {
        return heartbeatAppRepository.findAllByStatus(status);
    }

    //TODO
    public Flux<HeartbeatApp> findAndWatchByStatus(Status status) {
        ChangeStreamOptions options = ChangeStreamOptions.builder()
                .returnFullDocumentOnUpdate()
                .build();

        return reactiveMongoTemplate.changeStream("heartbeatApp", options, HeartbeatApp.class)
                .map(heartbeatAppChangeStreamEvent ->
                        Objects.requireNonNull(heartbeatAppChangeStreamEvent.getBody()));
    }

    public Mono<HeartbeatApp> findById(String id) {
        return heartbeatAppRepository.findById(id);
    }

    public Mono<HeartbeatApp> save(HeartbeatApp app) {
        return heartbeatAppRepository.save(app);
    }

    public Mono<HeartbeatApp> findByName(String name) {
        return heartbeatAppRepository.findByName(name);
    }

    public Flux<HeartbeatApp> findAllApps() {
        return heartbeatAppRepository.findAll();
    }

    public void deleteAppApps() {
        heartbeatAppRepository.deleteAll();
    }
}