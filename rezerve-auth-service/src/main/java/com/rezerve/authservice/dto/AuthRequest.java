package com.rezerve.authservice.dto;

import com.rezerve.authservice.dto.validator.CreateUserValidationGroup;
import com.rezerve.authservice.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is Required")
    @Size(min = 6)
    private String password;

    @NotNull(groups = CreateUserValidationGroup.class, message = "Role is Required")
    private Role role;

    @NotNull(groups = CreateUserValidationGroup.class, message = "Full Name is Required")
    private String fullName;

    @NotNull(groups = CreateUserValidationGroup.class, message = "Phone Number is Requried")
    @Size(min = 10, max = 10)
    private Long phoneNumber;
}
