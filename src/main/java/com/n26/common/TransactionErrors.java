package com.n26.common;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
* This error is raised when transaction is older then 60 seconds from current time.
* */

@ResponseStatus(HttpStatus.NO_CONTENT)
public class TransactionErrors extends Exception {}

