package com.example.imageservice.service;

import com.example.imageservice.model.Account;

public interface AuthenticationService {
    Account register(String email, String password);
}
