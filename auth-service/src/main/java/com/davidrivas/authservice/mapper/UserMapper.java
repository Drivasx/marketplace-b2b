package com.davidrivas.authservice.mapper;

import com.davidrivas.authservice.dto.SignupRequestDTO;
import com.davidrivas.authservice.dto.SignupResponseDTO;
import com.davidrivas.authservice.model.User;

import java.util.UUID;

public class UserMapper {

    public static User toEntity(SignupRequestDTO requestDTO) {
        User user = new User();
        user.setTenantId(UUID.fromString(requestDTO.tenantId()));
        user.setEmail(requestDTO.email());
        user.setPassword(requestDTO.password());
        user.setRole(requestDTO.role());
        return user;
    }

    public static SignupResponseDTO toDTO(User user) {
        return new SignupResponseDTO(
                user.getId().toString(),
                user.getTenantId().toString(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }
}
