package com.rezerve.authservice.dto;

import com.rezerve.authservice.model.enums.Role;
import lombok.Setter;

@Setter
public class AuthResponseDto {

    private String email;
    private Role role;
    private String fullName;
    private Long phoneNumber;

}
