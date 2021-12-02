package com.mycompany.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private UUID userUUID;
    private String email;
    private String role;
}
