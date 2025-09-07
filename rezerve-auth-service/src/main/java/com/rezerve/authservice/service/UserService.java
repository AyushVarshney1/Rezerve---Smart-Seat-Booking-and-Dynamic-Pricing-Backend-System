package com.rezerve.authservice.service;

import com.rezerve.authservice.dto.AuthRequest;
import com.rezerve.authservice.exception.UserNotFoundException;
import com.rezerve.authservice.mapper.UserMapper;
import com.rezerve.authservice.model.User;
import com.rezerve.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(AuthRequest authRequest) {
        User user = userMapper.toUser(authRequest);
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new UserNotFoundException("User with email: " + email + " not found");
        }
        return user.get();
    }
}
