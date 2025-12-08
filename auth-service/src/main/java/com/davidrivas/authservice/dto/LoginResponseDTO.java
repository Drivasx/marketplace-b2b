package com.davidrivas.authservice.dto;

public record LoginResponseDTO(String token, String refreshToken) {
}
