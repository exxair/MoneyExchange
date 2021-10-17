package com.exxair.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum Currency {
    PLN("PLN"),
    USD("USD");

    private final String value;
}
