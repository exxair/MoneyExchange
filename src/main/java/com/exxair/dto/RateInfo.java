package com.exxair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RateInfo {

    private String no;

    private String effectiveDate;

    private BigDecimal mid;
}
