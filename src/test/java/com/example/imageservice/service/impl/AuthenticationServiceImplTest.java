package com.example.imageservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.imageservice.model.Account;
import com.example.imageservice.service.AccountService;
import com.example.imageservice.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthenticationServiceImplTest {
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private String correctEmail;
    private String correctPassword;
    private Account user;

    @BeforeEach
    void setUp() {
        accountService = Mockito.mock(AccountServiceImpl.class);
        authenticationService = new AuthenticationServiceImpl(accountService);
        correctEmail = "user@gmail.com";
        correctPassword = "user1234";
        user = new Account();
        user.setEmail(correctEmail);
        user.setPassword(correctPassword);
        user.setRole("USER");
    }

    @Test
    void register_Ok() {
        when(accountService.add(user)).thenReturn(user);
        Account actual = authenticationService.register(correctEmail, correctPassword);
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void register_nullEmail_Ok() {
        user.setEmail(null);
        when(accountService.add(user)).thenReturn(user);
        Account actual = authenticationService.register(null, correctPassword);
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void register_nullPassword_Ok() {
        user.setPassword(null);
        when(accountService.add(user)).thenReturn(user);
        Account actual = authenticationService.register(correctEmail, null);
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }
}
