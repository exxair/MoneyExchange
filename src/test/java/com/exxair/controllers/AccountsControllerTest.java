package com.exxair.controllers;

import com.exxair.dto.AccountCreateRequest;
import com.exxair.dto.AccountExchangeRequest;
import com.exxair.dto.AccountInfoResponse;
import com.exxair.dto.SubAccountInfo;
import com.exxair.enums.Currency;
import com.exxair.services.AccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountsController.class)
class AccountsControllerTest {

    @MockBean
    AccountsService accountsService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testCreateAccount() throws Exception {
        AccountCreateRequest request = new AccountCreateRequest("Jan", "Kowalski", "11223344556", new BigDecimal(1000));
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(accountsService).createAccount(any(AccountCreateRequest.class));
    }

    @Test
    public void testGetAccount() throws Exception {
        AccountInfoResponse accountInfoResponse = AccountInfoResponse.builder()
                .name("Jan")
                .surname("Kowalski")
                .subAccounts(Collections.singletonList(new SubAccountInfo(Currency.PLN, new BigDecimal(1000))))
                .build();

        when(accountsService.getAccount(1)).thenReturn(accountInfoResponse);

        mockMvc.perform(get("/accounts/info/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subAccounts", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.subAccounts[0].currency", Matchers.is(Currency.PLN.getValue())))
                .andExpect(jsonPath("$.subAccounts[0].value", Matchers.is(1000)))
                .andExpect(jsonPath("$.name", Matchers.is("Jan")))
                .andExpect(jsonPath("$.surname", Matchers.is("Kowalski")));
    }

    @Test
    public void testExchange() throws Exception {
        AccountExchangeRequest request = new AccountExchangeRequest(Currency.PLN, new BigDecimal(1000));
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(patch("/accounts/exchange/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(accountsService).exchange(1, request);
    }

}