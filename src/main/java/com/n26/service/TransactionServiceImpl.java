package com.n26.service;

import com.n26.common.AheadOfTimeTransaction;
import com.n26.common.DataFormatError;
import com.n26.common.TransactionErrors;
import com.n26.common.Utility;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

// Implementation of transaction service

@Service
public class TransactionServiceImpl implements  TransactionService {

    @Autowired
    TransactionRepo repo;
    private final int DEADLINE = 60;
    private final TemporalUnit TEMPORAL_UNIT = ChronoUnit.SECONDS;
    /*
    * The idea is to append data to a queue for a particular time. Seeing transactions as a stream of data.
    * Evicting older data and creating materialized view when required.
    * */
    public void append(Transaction transaction) throws Exception {
        if (transaction.isValid()) {
            if (!Utility.inRange(transaction, DEADLINE, TEMPORAL_UNIT)) {
                throw new TransactionErrors();
            }
            if(Utility.aheadOfTime(transaction)) {
                throw new AheadOfTimeTransaction();
            }
            repo.save(transaction.getTransitionData());
        } else {
            throw new DataFormatError("Message format mismatch");
        }
    }

    // This function returns the materialized view.
    public Statistics getStats() {
        return repo.getStats();
    }

    // The function clears the map.
    public boolean clear() {
       return repo.clear();
    }
}
