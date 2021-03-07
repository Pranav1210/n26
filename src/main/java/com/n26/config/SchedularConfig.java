package com.n26.config;

import com.n26.service.ThreadPoolProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/*
* The scheduler runs periodic computation of materialized view of past 60 seconds transactions.
* */

@Configuration
public class SchedularConfig  implements SchedulingConfigurer {
    private int POOL_SIZE = 4;

    @Autowired
    ThreadPoolProvider threadPoolProvider;
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = threadPoolProvider.getThreadPool();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("calculator");
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.setDaemon(false);

        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}

