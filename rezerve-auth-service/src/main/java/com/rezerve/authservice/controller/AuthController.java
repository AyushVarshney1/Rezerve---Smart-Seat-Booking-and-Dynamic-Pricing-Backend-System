package com.rezerve.authservice.controller;

import com.rezerve.authservice.dto.AuthRequestDto;
import com.rezerve.authservice.dto.AuthResponseDto;
import com.rezerve.authservice.dto.validator.CreateUserValidationGroup;
import com.rezerve.authservice.mapper.UserMapper;
import com.rezerve.authservice.model.User;
import com.rezerve.authservice.service.AuthService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Validated({Default.class, CreateUserValidationGroup.class}) @RequestBody AuthRequestDto authRequestDto) {
        String token = authService.createUser(authRequestDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated({Default.class}) @RequestBody AuthRequestDto authRequestDto) {
        String token = authService.getToken(authRequestDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@RequestHeader("Authorization") String token){

        if(token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(token.substring(7)) ? ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/extract-user")
    public ResponseEntity<AuthResponseDto> extractUserFromToken(@RequestHeader("Authorization") String token){

        if(token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = authService.extractUser(token.substring(7));

        return ResponseEntity.ok(userMapper.toAuthResponseDto(user));
    }

    @GetMapping("/extract-role")
    public ResponseEntity<String> extractRoleFromToken(@RequestHeader("Authorization") String token){
        if(token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String role =  authService.extractRole(token.substring(7));

        return ResponseEntity.ok(role);
    }

}
