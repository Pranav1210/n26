package com.n26.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/*
* The transaction is stored in cache using this data model. This is pristine data with all validation already done.
* */


@Getter
@AllArgsConstructor
@ToString
public class TransactionData {
    private double amount;
    private long timestamp;
}
