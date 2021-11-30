package com.example.imageservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Account;
import com.example.imageservice.repository.AccountRepository;
import com.example.imageservice.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class AccountServiceImplTest {
    private AccountRepository accountRepository;
    private AccountService accountService;
    private Account user;
    private Account userNull;
    private List<Account> users;
    private List<Account> emptyList;
    private Long idNull;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountService = new AccountServiceImpl(accountRepository);
        user = new Account();
        user.setEmail("user@gmail.com");
        user.setRole("USER");
        user.setPassword("user1234");
        Account user2 = new Account();
        user2.setEmail("user2@gmail.com");
        user2.setRole("USER");
        user2.setPassword("user1234");
        users = List.of(user, user2);
        emptyList = new ArrayList<>();
        userNull = null;
        idNull = null;
    }

    @Test
    void add_Ok() {
        when(accountRepository.save(user)).thenReturn(user);
        Account actual = accountService.add(user);
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void add_null_NotOk() {
        when(accountRepository.save(userNull)).thenThrow(IllegalArgumentException.class);
        try {
            accountService.add(userNull);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void get_Ok() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(user));
        Account actual = accountService.get(1L);
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void get_notExist_NotOk() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findById(2L)).thenThrow(DataProcessingException.class);
        try {
            Account actual = accountService.get(2L);
            Assertions.assertNotNull(actual);
            assertEquals(user, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void get_null_NotOk() {
        when(accountRepository.findById(idNull)).thenThrow(IllegalArgumentException.class);
        try {
            accountService.get(idNull);
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void getAll_Ok() {
        when(accountRepository.findAll()).thenReturn(users);
        List<Account> actual = accountService.getAll();
        Assertions.assertNotNull(actual);
        assertEquals(users.size(), actual.size());
    }

    @Test
    void getAll_notFound_Ok() {
        when(accountRepository.findAll()).thenReturn(emptyList);
        List<Account> actual = accountService.getAll();
        Assertions.assertNotNull(actual);
        assertEquals(emptyList.size(), actual.size());
    }

    @Test
    void update_Ok() {
        user.setId(1L);
        when(accountRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(accountRepository.save(user)).thenReturn(user);
        Account actual = accountService.update(user);
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void update_notExist_NotOk() {
        when(accountRepository.findById(1L)).thenThrow(DataProcessingException.class);
        try {
            Account actual = accountService.update(user);
            Assertions.assertNotNull(actual);
            assertEquals(user, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void delete_Ok() {
        user.setId(1L);
        doNothing().doThrow(new IllegalArgumentException()).when(accountRepository).deleteById(user.getId());
        accountService.delete(user.getId());
        try {
            accountService.delete(user.getId());
        } catch (IllegalArgumentException e) {
            return;
        }
        Assertions.fail("Expected to receive IllegalArgumentException");
    }

    @Test
    void findByName_Ok() {

    }

    @Test
    void findByEmail() {
        when(accountRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Account actual = accountService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void findByEmail_notExist_notOk() {
        when(accountRepository.findByEmail(user.getEmail())).thenThrow(DataProcessingException.class);
        try {
            Account actual = accountService.findByEmail(user.getEmail());
            Assertions.assertNotNull(actual);
            assertEquals(user, actual);
        } catch (DataProcessingException e) {
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}
