package com.sam.heartbeat.service.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import com.sam.heartbeat.client.PulseClient;
import com.sam.heartbeat.model.Pulse;
import com.sam.heartbeat.repository.PulseRepository;
import com.sam.heartbeat.service.HeartbeatAppService;

@Slf4j
@Service
@RequiredArgsConstructor
@PersistJobDataAfterExecution
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HeartbeatJob implements Job {

    private final PulseClient pulseClient;
    private final PulseRepository pulseRepository;
    private final HeartbeatAppService heartbeatAppService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            pulseHeartbeatApps(jobDataMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("HeartbeatJob.execute: {}", e.getMessage());
        }
    }


    private void pulseHeartbeatApps(JobDataMap jobDataMap) {
        String id = (String) jobDataMap.get("appId");
        heartbeatAppService.findById(id)
                .switchIfEmpty(Mono.error(() -> new IllegalAccessException("No heartbeat app present for id: " + id)))
                .flatMap(pulseClient::pulseHeartbeatApp)
                .flatMap(pulseRepository::save)
                .filter(Pulse::isStateChange)
                .map(Pulse::getHeartbeatApp)
                .flatMap(heartbeatAppService::save)
                .subscribe();
    }


}
