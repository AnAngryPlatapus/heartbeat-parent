package com.sam.heartbeat.config;

import lombok.RequiredArgsConstructor;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@RequiredArgsConstructor
public class JobBeanFactoryConfig extends SpringBeanJobFactory {

    private final AutowireCapableBeanFactory beanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        final var job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }

}

