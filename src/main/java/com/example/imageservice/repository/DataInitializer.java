package com.example.imageservice.repository;

import com.example.imageservice.model.Account;
import com.example.imageservice.service.AccountService;
import javax.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class DataInitializer {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AccountService accountService, PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void inject() {
        Account admin = new Account();
        admin.setEmail("admin@i.ua");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        accountService.add(admin);
        Account user = new Account();
        user.setEmail("user@i.ua");
        user.setPassword(passwordEncoder.encode("user1234"));
        user.setRole("USER");
        accountService.add(user);
    }
}
