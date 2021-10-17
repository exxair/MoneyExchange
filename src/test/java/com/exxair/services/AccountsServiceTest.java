package com.exxair.services;

import com.exxair.dto.AccountCreateRequest;
import com.exxair.dto.AccountExchangeRequest;
import com.exxair.dto.AccountInfoResponse;
import com.exxair.enums.Currency;
import com.exxair.exceprions.AccountExistsException;
import com.exxair.exceprions.InsufficientFundsException;
import com.exxair.model.Account;
import com.exxair.repositories.AccountsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountsServiceTest {

    public static final String PESEL = "11223344556";

    @InjectMocks
    private AccountsService sut;

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAccount() {
        String name = "Jan";
        String surname = "Kowalski";
        BigDecimal initialValue = new BigDecimal(1000);
        AccountCreateRequest request = new AccountCreateRequest(name, surname, PESEL, initialValue);
        when(accountsRepository.findByPesel(PESEL)).thenReturn(Optional.empty());

        sut.createAccount(request);

        verify(accountsRepository).save(any(Account.class));
    }

    @Test()
    public void accountExists() {
        String name = "Jan";
        String surname = "Kowalski";
        BigDecimal initialValue = new BigDecimal(1000);
        AccountCreateRequest request = new AccountCreateRequest(name, surname, PESEL, initialValue);
        when(accountsRepository.findByPesel(PESEL)).thenReturn(Optional.of(new Account(name, surname, PESEL, initialValue)));

        Assertions.assertThrows(AccountExistsException.class, () -> sut.createAccount(request));
    }

    @Test
    public void getAccount() {
        String name = "Jan";
        String surname = "Kowalski";
        BigDecimal value = new BigDecimal(1000);
        Account account = new Account(name, surname, PESEL, value);
        when(accountsRepository.getById(1L)).thenReturn(account);

        AccountInfoResponse result = sut.getAccount(1L);

        Assertions.assertEquals(name, result.getName());
        Assertions.assertEquals(surname, result.getSurname());
        Assertions.assertEquals(2, result.getSubAccounts().size());
    }

    @Test
    public void exchange() {
        String name = "Jan";
        String surname = "Kowalski";
        BigDecimal value = new BigDecimal(1000);
        Account account = new Account(name, surname, PESEL, value);
        AccountExchangeRequest request = new AccountExchangeRequest(Currency.PLN, new BigDecimal(100));
        when(accountsRepository.getById(1L)).thenReturn(account);
        when(exchangeRateService.getUsdExchangeRate()).thenReturn(new BigDecimal(4));

        sut.exchange(1L, request);

        Assertions.assertEquals(2, account.getSubaccounts().size());
        Assertions.assertEquals(900.0, account.getSubaccounts().get(Currency.PLN).doubleValue());
        Assertions.assertEquals(25.0, account.getSubaccounts().get(Currency.USD).doubleValue());
        verify(accountsRepository).save(any(Account.class));
    }

    @Test
    public void exchangeInsufficientFunds() {
        String name = "Jan";
        String surname = "Kowalski";
        BigDecimal value = new BigDecimal(500);
        Account account = new Account(name, surname, PESEL, value);
        AccountExchangeRequest request = new AccountExchangeRequest(Currency.PLN, new BigDecimal(1000));
        when(accountsRepository.getById(1L)).thenReturn(account);

        Assertions.assertThrows(InsufficientFundsException.class, () -> sut.exchange(1L, request));
    }
}