package com.rezerve.authservice.mapper;

import com.rezerve.authservice.dto.AuthRequestDto;
import com.rezerve.authservice.dto.AuthResponseDto;
import com.rezerve.authservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toUser(AuthRequestDto authRequestDto) {
        User user = new User();
        user.setEmail(authRequestDto.getEmail());
        user.setRole(authRequestDto.getRole());
        user.setFullName(authRequestDto.getFullName());
        user.setPhoneNumber(authRequestDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
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
