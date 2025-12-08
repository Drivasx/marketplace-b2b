package com.davidrivas.authservice.service;

import com.davidrivas.authservice.dto.SignupResponseDTO;
import com.davidrivas.authservice.mapper.UserMapper;
import com.davidrivas.authservice.model.User;
import com.davidrivas.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public SignupResponseDTO save(User user) {
        return UserMapper.toDTO(userRepository.save(user));
    }
}
