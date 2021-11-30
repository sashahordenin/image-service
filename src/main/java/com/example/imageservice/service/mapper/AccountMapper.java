package com.example.imageservice.service.mapper;

import com.example.imageservice.dto.request.AccountRequestDto;
import com.example.imageservice.dto.response.AccountResponseDto;
import com.example.imageservice.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper implements ResponseDtoMapper<AccountResponseDto, Account>,
        RequestDtoMapper<AccountRequestDto, Account> {
    @Override
    public AccountResponseDto mapToDto(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setEmail(account.getEmail());
        accountResponseDto.setRole(account.getRole());
        accountResponseDto.setCreationDateTime(account.getCreationDate().toString());
        accountResponseDto.setUpdateDateTime(account.getUpdateDate().toString());
        return accountResponseDto;
    }

    @Override
    public Account mapToModel(AccountRequestDto dto) {
        Account account = new Account();
        account.setEmail(dto.getEmail());
        account.setPassword(dto.getPassword());
        return account;
    }
}
