package com.rezerve.authservice.dto;

import com.rezerve.authservice.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {

    private String email;
    private Role role;
    private String fullName;
    private String phoneNumber;

}
