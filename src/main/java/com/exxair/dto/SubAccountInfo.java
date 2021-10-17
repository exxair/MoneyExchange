package com.exxair.dto;

import com.exxair.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SubAccountInfo {

    private Currency currency;

    private BigDecimal value;
}
