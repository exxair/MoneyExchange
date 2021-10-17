package com.exxair.dto;

import com.exxair.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExchangeRateResponse {

    private String table;

    private String currency;

    private Currency code;

    private List<RateInfo> rates;
}
