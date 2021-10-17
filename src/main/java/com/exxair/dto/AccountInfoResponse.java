package com.exxair.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Getter
@Builder
public class AccountInfoResponse {

    private String name;

    private String surname;

    private Collection<SubAccountInfo> subAccounts;
}
