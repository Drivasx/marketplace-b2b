package com.davidrivas.authservice.dto;

public record SignupRequestDTO(String tenantId, String email, String password, String role) {
}
