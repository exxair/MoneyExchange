package com.exxair.services;

import com.exxair.dto.ExchangeRateResponse;
import com.exxair.dto.RateInfo;
import com.exxair.enums.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;

class ExchangeRateServiceTest {

    public static final String NBP_USD_RATE_URL = "http://api.nbp.pl/api/exchangerates/rates/a/usd/";

    @InjectMocks
    private ExchangeRateService sut;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsdExchangeRate() {
        ExchangeRateResponse mockResponse = new ExchangeRateResponse("A", "dolar amerykański", Currency.USD,
                Collections.singletonList(new RateInfo("201/A/NBP/2021", "2021-10-15", new BigDecimal(4))));

        when(restTemplate.getForEntity(NBP_USD_RATE_URL, ExchangeRateResponse.class)).thenReturn(ResponseEntity.of(Optional.of(mockResponse)));

        BigDecimal result = sut.getUsdExchangeRate();

        Assertions.assertEquals(new BigDecimal(4), result);
    }
}