package com.n26.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
* The error is raised when transaction ahead of current time is triggered
* */

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class AheadOfTimeTransaction extends Exception {
}
