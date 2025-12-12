package com.davidrivas.authservice.mapper;

import com.davidrivas.authservice.dto.SignupRequestDTO;
import com.davidrivas.authservice.dto.SignupResponseDTO;
import com.davidrivas.authservice.model.User;


public class UserMapper {

    public static User toEntity(SignupRequestDTO requestDTO) {
        User user = new User();
        user.setEmail(requestDTO.email());
        user.setPassword(requestDTO.password());
        return user;
    }

    public static SignupResponseDTO toDTO(User user, String token, String refreshToken) {
        return new SignupResponseDTO(
                user.getId().toString(),
                user.getTenantId().toString(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                token,
                refreshToken);
    }
}
