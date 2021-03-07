package com.n26.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

// Theadpool to run evicting of older values in background

@Service
public class ThreadPoolProviderImpl implements ThreadPoolProvider {
    private static final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    public ThreadPoolTaskScheduler getThreadPool() {
        return threadPoolTaskScheduler;
    }
}
