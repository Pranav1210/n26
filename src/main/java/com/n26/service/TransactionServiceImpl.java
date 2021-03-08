package com.n26.service;

import com.n26.common.AheadOfTimeTransaction;
import com.n26.common.DataFormatError;
import com.n26.common.TransactionErrors;
import com.n26.common.Utility;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repository.TransactionRepo;
import com.n26.repository.TransactionRepoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.n26.common.Constants.DEADLINE;
import static com.n26.common.Constants.TEMPORAL_UNIT;
// Implementation of transaction service

@Service
public class TransactionServiceImpl implements  TransactionService {
    @Autowired
    TransactionRepo repo;

    private final static Logger log =  LoggerFactory.getLogger(TransactionRepoImpl.class);

    /*
    * The idea is to append data to a queue for a particular time. Seeing transactions as a stream of data.
    * Evicting older data and creating materialized view when required.
    * */
    public void append(Transaction transaction) throws Exception {
        if (transaction.isValid()) {
            if (!Utility.inRange(transaction, DEADLINE, TEMPORAL_UNIT)) {
                if(log.isErrorEnabled()) log.error("Received transaction older than 60 seconds");
                throw new TransactionErrors();
            }
            if(Utility.aheadOfTime(transaction)) {
                if(log.isErrorEnabled()) log.error("Received transaction ahead in time");
                throw new AheadOfTimeTransaction();
            }
            repo.save(transaction.getTransactionData());
        } else {
            if(log.isErrorEnabled()) log.error("Received invalid transaction with value as {}", transaction);
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
