package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

// Contract for controller to connect to data repo.
public interface TransactionService {
    void append(Transaction transactionData) throws Exception;

    Statistics getStats();

    boolean clear();
}
