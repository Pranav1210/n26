package com.n26.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.n26.model.Statistics;
import com.n26.model.TransactionData;
import com.n26.service.ThreadPoolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* Implementation of Repo which is backed by Guava Cache. Mainly using Guava cache for easy eviction of values
* older than 60 seconds in the Map.
* */
@Component
public class TransactionRepoImpl implements TransactionRepo {
    // Using a queue to handle multiple transaction in a single millisecond.
    private final ConcurrentMap<Long, BlockingQueue<Double>> repo;
    private final Cache<Long, BlockingQueue<Double>> cache;
    private final static Logger log =  LoggerFactory.getLogger(TransactionRepoImpl.class);
    private Statistics materializedView = new Statistics();

    private final int DEADLINE = 60;
    private final TimeUnit UNIT = TimeUnit.SECONDS;
    private final TemporalUnit TEMPORAL_UNIT = ChronoUnit.SECONDS;


    @Autowired
    ThreadPoolProvider threadPoolProvider;

    public TransactionRepoImpl() {
        this.cache = CacheBuilder.newBuilder()
                .initialCapacity(DEADLINE)
                .expireAfterWrite(DEADLINE, UNIT)
                .build();
        this.repo = cache.asMap();
    }

    public void save(TransactionData td) {
        if (!repo.containsKey(td.getTimestamp())) {
            synchronized (repo) {
                repo.putIfAbsent(td.getTimestamp(), new LinkedBlockingQueue<>());
            }
        }
        repo.get(td.getTimestamp()).add(td.getAmount());
        // Evicting values older than deadline
        threadPoolProvider.getThreadPool().execute(() ->
                evictValues(Instant.now().minus(DEADLINE, TEMPORAL_UNIT).toEpochMilli()));
    }

    // The rate should be less than minimum tolerance of error. Lower the tolerance, more is the load on system.
    @Scheduled(fixedDelay = 500)
    private void evictOldValues() {
        evictValues(Instant.now().minus(DEADLINE, TEMPORAL_UNIT).toEpochMilli());
    }

//    @Scheduled(fixedRate = 1000)
//    private void updateValue() {
//        generateView();
//    }

    private void evictValues(long olderThan) {
        log.trace("Evicting values older than {}", olderThan);
        repo.keySet().stream().filter(deadLine -> deadLine < olderThan).forEach(repo::remove);
    }

    private void generateView() {
        log.info("Calculating materialized view of transactions");
        long cutOffEpoch = Instant.now().minus(DEADLINE, TEMPORAL_UNIT).toEpochMilli();

        DoubleSummaryStatistics dss = repo.entrySet()
                .stream().filter(kv -> kv.getKey() > cutOffEpoch).flatMap(kv -> {
                    return kv.getValue().stream();
                }).mapToDouble(Double::valueOf).summaryStatistics();
        materializedView = new Statistics(dss);
    }

    public Statistics getStats() {
        if (materializedView == null) {
            return new Statistics();
        }
        generateView();
        log.info("Returning materialized view {}", materializedView);
        return materializedView;
    }

    public boolean clear() {
        try {
            repo.clear();
            materializedView = new Statistics();
            return true;
        } catch (Exception ex) {
            log.error("Failed to clear store");
            return false;
        }
    }
}
