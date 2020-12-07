package com.sam.heartbeat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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
    private final HeartbeatJobUtil heartbeatJobUtil;

    private final HeartbeatAppRepository heartbeatAppRepository;

    public Mono<HeartbeatApp> onAppInit(HeartbeatApp heartbeatApp) {
        return heartbeatAppRepository.findByName(heartbeatApp.getName())
                .switchIfEmpty(Mono.defer(() -> startPulse(heartbeatAppRepository.save(heartbeatApp))))
                .flatMap(app -> {
                    app.setStatus(Status.UP);
                    return heartbeatAppRepository.save(app);
                });

    }

    private Mono<HeartbeatApp> startPulse(Mono<HeartbeatApp> heartbeatAppMono) {
        return heartbeatAppMono.map(heartbeatApp -> {
            JobDetail heartbeatJobDetail = heartbeatJobUtil.buildJobDetail(heartbeatApp);
            try {
                scheduler.scheduleJob(heartbeatJobDetail, heartbeatJobUtil.buildTrigger(heartbeatJobDetail));
            } catch (SchedulerException se) {
                se.printStackTrace();
            }
            return heartbeatApp;
        });
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
