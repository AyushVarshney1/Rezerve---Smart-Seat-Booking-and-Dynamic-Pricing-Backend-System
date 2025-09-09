package com.rezerve.authservice.service;

import com.rezerve.authservice.dto.AuthRequest;
import com.rezerve.authservice.exception.UserNotFoundException;
import com.rezerve.authservice.model.User;
import com.rezerve.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String createUser(AuthRequest authRequest) {
        User user = userService.createUser(authRequest);

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

    public boolean validateToken(String token){
        try{
            jwtUtil.validateToken(token);
            return true;
        }catch(JwtException e){
            return false;
        }
    }

    public User extractUser(String token) {
        String email = jwtUtil.extractEmail(token);
        return userService.findByEmail(email);
    }

    public String extractRole(String token){
        String role = jwtUtil.extractRole(token);
        return role;
    }
}
