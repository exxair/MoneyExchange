package com.exxair.services;

import com.exxair.dto.ExchangeRateResponse;
import com.exxair.exceprions.ExchangeRateException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ExchangeRateService {

    public static final String NBP_USD_RATE_URL = "http://api.nbp.pl/api/exchangerates/rates/a/usd/";

    public BigDecimal getUsdExchangeRate() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ExchangeRateResponse> result = restTemplate.getForEntity(NBP_USD_RATE_URL, ExchangeRateResponse.class);

        return Optional.ofNullable(result.getBody())
                .map(b -> b.getRates().get(0).getMid())
                .orElseThrow(() -> new ExchangeRateException("There is a problem retrieving current exchange rate"));
    }
}
