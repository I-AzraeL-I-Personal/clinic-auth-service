package com.mycompany.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EditUserDto {

    @Email(message = "must be a well-formed email address")
    @NotNull(message = "email cannot be null")
    @Size(min = 3, max = 200, message = "email must be between {min} and {max} characters")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 16, message = "password must be between {min} and {max} characters")
    private String password;
}
