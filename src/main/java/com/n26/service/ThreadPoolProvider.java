package com.n26.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public interface ThreadPoolProvider {
    ThreadPoolTaskScheduler getThreadPool();
}
