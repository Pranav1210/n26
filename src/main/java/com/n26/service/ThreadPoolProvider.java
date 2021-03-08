package com.n26.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/*
* The interface provides the threadpool on which eviction of older records is done.
* */

public interface ThreadPoolProvider {
    ThreadPoolTaskScheduler getThreadPool();
}
