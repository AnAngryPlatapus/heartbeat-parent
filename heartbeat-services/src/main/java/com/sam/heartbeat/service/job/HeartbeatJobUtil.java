package com.sam.heartbeat.service.job;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sam.heartbeat.model.HeartbeatApp;

@Slf4j
@Service
public class HeartbeatJobUtil {

    @Value("${heartbeat.trigger.expression}")
    private String heartbeatTriggerExpression;

    private JobKey heartbeatJobKey(HeartbeatApp heartbeatApp) {
        return JobKey.jobKey(heartbeatApp.getName());
    }

    public JobDetail buildJobDetail(HeartbeatApp heartbeatApp) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduledApp", heartbeatApp.getId());
        return newJob(HeartbeatJob.class)
                .withIdentity(heartbeatJobKey(heartbeatApp))
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public Trigger buildTrigger(JobDetail jobDetail) {
        return newTrigger().forJob(jobDetail)
                .withIdentity(TriggerKey.triggerKey("heartbeatTrigger", "heartbeatApps"))
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1))
                .build();
    }


}
