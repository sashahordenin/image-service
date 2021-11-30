package com.example.imageservice.controller;

import com.example.imageservice.dto.request.AccountRequestDto;
import com.example.imageservice.dto.response.AccountResponseDto;
import com.example.imageservice.model.Account;
import com.example.imageservice.service.AccountService;
import com.example.imageservice.service.PageService;
import com.example.imageservice.service.mapper.AccountMapper;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final PageService<AccountResponseDto> pageService;

    public AccountController(AccountService accountService,
                             AccountMapper accountMapper,
                             PageService<AccountResponseDto> pageService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.pageService = pageService;
    }

    @GetMapping
    public Page<AccountResponseDto> getAll(Pageable pageable) {
        return pageService.getPageableResult(pageable, accountService.getAll()
                .stream()
                .map(accountMapper::mapToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public AccountResponseDto get(@PathVariable Long id) {
        return accountMapper.mapToDto(accountService.get(id));
    }

    @PutMapping
    public AccountResponseDto update(Authentication auth,
                                     @RequestBody @Valid AccountRequestDto accountRequestDto) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Account account = accountService.findByEmail(userDetails.getUsername());
        account.setEmail(accountRequestDto.getEmail());
        account.setPassword(accountRequestDto.getPassword());
        return accountMapper.mapToDto(accountService.update(account));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        accountService.delete(id);
    }
}
