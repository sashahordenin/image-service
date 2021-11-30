package com.example.imageservice.service.impl;

import com.example.imageservice.model.Account;
import com.example.imageservice.service.AccountService;
import com.example.imageservice.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountService accountService;

    public AuthenticationServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Account register(String email, String password) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        account.setRole("USER");
        accountService.add(account);
        return account;
    }
}
