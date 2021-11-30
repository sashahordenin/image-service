package com.example.imageservice.dto.response;

import lombok.Data;

@Data
public class AccountResponseDto {
    private Long id;
    private String email;
    private String role;
    private String creationDateTime;
    private String updateDateTime;
}
