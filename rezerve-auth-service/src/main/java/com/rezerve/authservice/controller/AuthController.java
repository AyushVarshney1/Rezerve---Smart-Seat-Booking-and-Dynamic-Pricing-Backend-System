package com.rezerve.authservice.controller;

import com.rezerve.authservice.dto.AuthRequest;
import com.rezerve.authservice.dto.validator.CreateUserValidationGroup;
import com.rezerve.authservice.service.AuthService;
import com.rezerve.authservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Validated({Default.class, CreateUserValidationGroup.class}) @RequestBody AuthRequest authRequest) {
        String token = authService.createUser(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated({Default.class}) @RequestBody AuthRequest authRequest) {
        String token = authService.getToken(authRequest);
        return ResponseEntity.ok(token);
    }

}
