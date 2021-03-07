package com.n26.controller;

import com.n26.common.AheadOfTimeTransaction;
import com.n26.common.DataFormatError;
import com.n26.common.TransactionErrors;
import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @MockBean
    private TransactionService service;

    @Autowired
    private MockMvc mockMvc;

    static String data =  "{\n" + "\"amount\": \"12.3343\",\n" + "\"timestamp\": \"2018-07-17T09:59:51.312Z\"\n" + "}";

    @Test
    public void postRequestWithNoDataShouldGive_400() throws Exception {
        this.mockMvc.perform(
                post("/transactions")).andExpect(status().is(400));
    }

    @Test
    public void postCorrectRquestShouldGive_201() throws Exception {
        Transaction t = new Transaction("12.0", "New Time");

        doNothing().when(service).append(any());
        this.mockMvc.perform(
                post("/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(data)).andExpect(status().is(201));

    }

    @Test
    public void postFutureValueShouldGive_422() throws Exception {
        Mockito.doThrow(new AheadOfTimeTransaction()).when(service).append(any());
        this.mockMvc.perform(
                post("/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(data)).andExpect(status().is(422));

    }

    @Test
    public void postOlderValueShouldGive_204() throws Exception {
        Mockito.doThrow(new TransactionErrors()).when(service).append(any());
        this.mockMvc.perform(
                post("/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(data)).andExpect(status().is(204));

    }

    @Test
    public void postWithIllegalValuesShouldReturn_422() throws Exception {
        Mockito.doThrow(new DataFormatError("Wrong Data")).when(service).append(any());
        this.mockMvc.perform(
                post("/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(data)).andExpect(status().is(422));

    }

    @Test
    public void clearDataShouldReturn_201() throws Exception {
        when(service.clear()).thenReturn(true);
        this.mockMvc.perform(
                delete("/transactions")).andExpect(status().is(204));
    }

    @Test
    public void getStatsShouldReturnEmprtyStat() throws Exception {
        when(service.getStats()).thenReturn(new Statistics());
        this.mockMvc.perform(
                get("/statistics"))
                .andExpect(status().is(200));
    }
}
