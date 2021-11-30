package com.example.imageservice.service;

import com.example.imageservice.model.Account;
import java.util.List;

public interface AccountService {
    Account add(Account account);

    Account get(Long id);

    List<Account> getAll();

    Account update(Account account);

    void delete(Long id);

    Account findByEmail(String email);
}
