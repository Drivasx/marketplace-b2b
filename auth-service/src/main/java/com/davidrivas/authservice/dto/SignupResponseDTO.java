package com.davidrivas.authservice.dto;

public record SignupResponseDTO(String id, String tenantId, String email, String password, String role, String token, String refreshToken) {
}
