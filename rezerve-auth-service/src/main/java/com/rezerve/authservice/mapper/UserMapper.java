package com.rezerve.authservice.mapper;

import com.rezerve.authservice.dto.AuthRequest;
import com.rezerve.authservice.dto.AuthResponseDto;
import com.rezerve.authservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    public User toUser(AuthRequest authRequest) {
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setRole(authRequest.getRole());
        user.setFullName(authRequest.getFullName());
        user.setPhoneNumber(authRequest.getPhoneNumber());
        return user;
    }

    public AuthResponseDto toAuthResponseDto(User user) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setEmail(user.getEmail());
        authResponseDto.setRole(user.getRole());
        authResponseDto.setFullName(user.getFullName());
        authResponseDto.setPhoneNumber(user.getPhoneNumber());
        return authResponseDto;
    }
}
