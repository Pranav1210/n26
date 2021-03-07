package com.n26.repository;

import com.n26.common.AheadOfTimeTransaction;
import com.n26.common.DataFormatError;
import com.n26.common.TransactionErrors;
import com.n26.model.Statistics;
import com.n26.model.TransactionData;


// Contract to connect to Repo which contains materialized view of data
public interface TransactionRepo {

    void save(TransactionData transactionData) throws DataFormatError, TransactionErrors, AheadOfTimeTransaction;

    Statistics getStats();

    boolean clear();
}
