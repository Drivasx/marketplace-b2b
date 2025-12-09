package com.davidrivas.authservice.dto;

import java.util.List;

public record TokenValidationResponse(
        boolean valid,
        String userId,
        String tenantId,
        List<String> roles) {
}
