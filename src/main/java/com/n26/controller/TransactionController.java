package com.n26.controller;


import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/*
* Controller to handle all incoming requests defined in documentation.
* */

@RestController
public class TransactionController {

    private final static Logger log = (Logger) LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService service;

    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity saveTransaction(@RequestBody Transaction data) throws Exception {
        service.append(data);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public ResponseEntity<Statistics> getStatistics(){
        Statistics stats = service.getStats();
        return ResponseEntity.ok().body(stats);
    }

    @RequestMapping(path = "/transactions", method = RequestMethod.DELETE)
    public ResponseEntity clearAllTransactions(){
        service.clear();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}