package com.example.imageservice.service.impl;

import com.example.imageservice.exception.DataProcessingException;
import com.example.imageservice.model.Account;
import com.example.imageservice.repository.AccountRepository;
import com.example.imageservice.service.AccountService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account add(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account get(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new DataProcessingException("Can't find account by id: "
                        + id));
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account update(Account account) {
        accountRepository.findById(account.getId()).orElseThrow(
                () -> new DataProcessingException("Can't find account by id: "
                        + account.getId()));
        return accountRepository.save(account);
    }

    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new DataProcessingException("Can't find account with email : "
                + email));
    }
}
