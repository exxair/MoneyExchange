package com.exxair.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotNull
    @Pattern(regexp = "^\\d{11}$")
    private String pesel;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal initialValue;
}
