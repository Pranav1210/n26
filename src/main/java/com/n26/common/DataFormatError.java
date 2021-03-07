package com.n26.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Timestamp provided in the JSON is of not proper format
 * */

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DataFormatError extends Exception {
    public DataFormatError(String msg) {
        super(msg);
    }
}
