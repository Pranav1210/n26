package com.n26.common;

import com.n26.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.TemporalUnit;

@Component
public class Utility {
    public static  boolean inRange(Transaction transaction, long amountToSubtract, TemporalUnit unit) {
        return transaction
                .getInitTime().minus(amountToSubtract, unit)
                .isBefore(
                        Instant.parse(String.valueOf(transaction.getTimestamp())));
    }

    public static  boolean aheadOfTime(Transaction transaction) {
        return transaction
                .getInitTime()
                .isBefore(Instant.parse(String.valueOf(transaction.getTimestamp())));
    }
}
