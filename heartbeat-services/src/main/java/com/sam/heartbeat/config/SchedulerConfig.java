package com.sam.heartbeat.config;

import lombok.SneakyThrows;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

    @Value("classpath:quartz.properties")
    private Resource quartzProperties;

    @Bean
    @SneakyThrows
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setQuartzProperties(PropertiesLoaderUtils.loadProperties(quartzProperties));
        schedulerFactoryBean.setJobFactory(new JobBeanFactoryConfig(applicationContext.getAutowireCapableBeanFactory()));
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler(ApplicationContext applicationContext) throws SchedulerException {
        Scheduler scheduler = schedulerFactory(applicationContext).getScheduler();
        scheduler.start();
        return scheduler;
    }
}
