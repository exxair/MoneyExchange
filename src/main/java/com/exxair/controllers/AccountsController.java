package com.exxair.controllers;

import com.exxair.dto.AccountCreateRequest;
import com.exxair.dto.AccountExchangeRequest;
import com.exxair.dto.AccountInfoResponse;
import com.exxair.services.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("accounts/")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    @PostMapping(path = "create")
    public ResponseEntity<Void> createAccount(@Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        this.accountsService.createAccount(accountCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "info/{accountId}")
    public ResponseEntity<AccountInfoResponse> getAccount(@PathVariable long accountId) {
        return ResponseEntity.ok(this.accountsService.getAccount(accountId));
    }

    @PatchMapping(path = "exchange/{accountId}")
    public ResponseEntity<Void> exchange(@PathVariable long accountId, @Valid @RequestBody AccountExchangeRequest accountExchangeRequest) {
        this.accountsService.exchange(accountId, accountExchangeRequest);
        return ResponseEntity.ok().build();
    }
}
