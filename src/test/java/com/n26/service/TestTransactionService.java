package com.n26.service;


import com.n26.common.AheadOfTimeTransaction;
import com.n26.common.DataFormatError;
import com.n26.common.TransactionErrors;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repository.TransactionRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(TransactionServiceImpl.class)
public class TestTransactionService {
    @MockBean
    TransactionRepo transactionRepo;

    @Autowired
    private TransactionService transactionService;


    @TestConfiguration
    static class TransactionServiceImplTestContextConfiguration {
        @Bean
        public TransactionService transactionService() {
            return new TransactionServiceImpl();
        }
    }


    @Test
    public void clearTransactionShouldCallClearRepo() {
        when(transactionRepo.clear()).thenReturn(true);
        assertEquals(transactionService.clear(), Boolean.TRUE);
    }

    @Test
    public void getStatsShouldReturnEmptyStats() {
        Statistics st  = new Statistics();
        when(transactionRepo.getStats()).thenReturn(st);
        assertEquals(transactionService.getStats(), st);
    }

    @Test(expected = DataFormatError.class)
    public void callingAppendWithEmptyNumberShouldReturnDataFormatError() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXX");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String nowAsISO = df.format(new Date(System.currentTimeMillis()));
       transactionService.append(new Transaction("Name", nowAsISO));
    }

    @Test(expected = DataFormatError.class)
    public void callingAppendWithWrongTimeShouldReturnDataFormatError() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXX");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String nowAsISO = df.format(new Date(System.currentTimeMillis()));
        transactionService.append(new Transaction("12.0", "nowAsISO"));
    }

    @Test(expected = AheadOfTimeTransaction.class)
    public void callingAppendWithFutureDataShouldReturnAOTException() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXX");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String nowAsISO = df.format(new Date(System.currentTimeMillis() + 3600000));
        transactionService.append(new Transaction("1.00", nowAsISO));
    }

    @Test(expected = TransactionErrors.class)
    public void callingAppendOnOlderTransactionShouldReturnTransactionError() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXX");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String nowAsISO = df.format(new Date(System.currentTimeMillis() - (120 * 1000)));
        transactionService.append(new Transaction("1.00", nowAsISO));
    }


}
