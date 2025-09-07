package com.rezerve.authservice.service;

import com.rezerve.authservice.dto.AuthRequest;
import com.rezerve.authservice.model.User;
import com.rezerve.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String createUser(AuthRequest authRequest) {
        User user = userService.createUser(authRequest);
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole().toString());

        return token;
    }

    public String getToken(AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());

        if(!passwordEncoder.matches(authRequest.getPassword(),user.getPassword())){
            throw new BadCredentialsException("Incorrect credentials. Check if email or password is correct.");
        }

        String token =  jwtUtil.generateToken(user.getEmail(),user.getRole().toString());

        return token;

    }
}
