package com.example.imageservice.controller;

import com.example.imageservice.dto.request.AccountRequestDto;
import com.example.imageservice.dto.response.AccountResponseDto;
import com.example.imageservice.model.Account;
import com.example.imageservice.service.AuthenticationService;
import com.example.imageservice.service.mapper.AccountMapper;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;
    private final AccountMapper accountMapper;

    public AuthenticationController(AuthenticationService authService,
                                    AccountMapper accountMapper) {
        this.authService = authService;
        this.accountMapper = accountMapper;
    }

    @PostMapping("/register")
    public AccountResponseDto register(@RequestBody @Valid AccountRequestDto requestDto) {
        Account account = authService.register(requestDto.getEmail(), requestDto.getPassword());
        return accountMapper.mapToDto(account);
    }
}
