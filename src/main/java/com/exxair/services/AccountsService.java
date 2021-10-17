package com.exxair.services;

import com.exxair.dto.AccountCreateRequest;
import com.exxair.dto.AccountExchangeRequest;
import com.exxair.dto.AccountInfoResponse;
import com.exxair.dto.SubAccountInfo;
import com.exxair.enums.Currency;
import com.exxair.exceprions.AccountExistsException;
import com.exxair.exceprions.InsufficientFundsException;
import com.exxair.model.Account;
import com.exxair.repositories.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountsService {

    private final AccountsRepository accountsRepository;
    private final ExchangeRateService exchangeRateService;

    @Transactional
    public void createAccount(AccountCreateRequest accountCreateRequest) {
        accountsRepository.findByPesel(accountCreateRequest.getPesel()).ifPresent(a -> {
            throw new AccountExistsException("Account for this user already exists");
        });

        Account account = new Account(accountCreateRequest.getName(), accountCreateRequest.getSurname(), accountCreateRequest.getPesel(), accountCreateRequest.getInitialValue());
        accountsRepository.save(account);
    }

    @Transactional(readOnly = true)
    public AccountInfoResponse getAccount(long accountId) {
        Account account = accountsRepository.getById(accountId);
        return AccountInfoResponse
                .builder()
                .name(account.getName())
                .surname(account.getSurname())
                .subAccounts(account.getSubaccounts().entrySet().stream().map(e -> new SubAccountInfo(e.getKey(), e.getValue())).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void exchange(long accountId, AccountExchangeRequest accountExchangeRequest) {
        Account account = accountsRepository.getById(accountId);
        Map<Currency, BigDecimal> subAccounts = account.getSubaccounts();

        validateAmount(subAccounts, accountExchangeRequest);

        BigDecimal usdExchangeRate = exchangeRateService.getUsdExchangeRate();
        BigDecimal amount = accountExchangeRequest.getAmount();

        switch (accountExchangeRequest.getFromCurrency()) {
            case USD:
                subAccounts.put(Currency.USD, subAccounts.get(Currency.USD).subtract(amount));
                subAccounts.put(Currency.PLN, subAccounts.get(Currency.PLN).add(amount.multiply(usdExchangeRate)));
            case PLN:
                subAccounts.put(Currency.PLN, subAccounts.get(Currency.PLN).subtract(amount));
                subAccounts.put(Currency.USD, subAccounts.get(Currency.USD).add(amount.divide(usdExchangeRate, 2, RoundingMode.HALF_EVEN)));
        }
        accountsRepository.save(account);
    }

    private void validateAmount(Map<Currency, BigDecimal> subAccounts, AccountExchangeRequest accountExchangeRequest) {
        if (subAccounts.get(accountExchangeRequest.getFromCurrency()).compareTo(accountExchangeRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds to complete operation");
        }
    }
}
