package com.n26.model;


import com.n26.common.DataFormatError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/*
* Data model to accept incoming transaction requests. All validations are done on this data model.
* */

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Transaction {
    Object amount;
    Object timestamp;


    private final Instant initTime = Instant.now();
    public boolean isValid() {
        try {
            Double.valueOf(String.valueOf(this.amount));
            Instant.parse(String.valueOf(this.getTimestamp()));
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public TransactionData getTransitionData() throws DataFormatError {
        if (isValid()) {
            return new TransactionData(Double.parseDouble(String.valueOf(this.amount)),
                    Instant.parse(String.valueOf(this.getTimestamp())).toEpochMilli());
        } else  {
            throw new DataFormatError("Invalid transaction");
        }
    }
}
