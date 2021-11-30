package com.example.imageservice.dto.request;

import com.example.imageservice.lib.FieldsValueMatch;
import com.example.imageservice.lib.ValidEmail;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
public class AccountRequestDto {
    @ValidEmail
    private String email;
    @Size(min = 8, max = 40)
    private String password;
    private String repeatPassword;
}
