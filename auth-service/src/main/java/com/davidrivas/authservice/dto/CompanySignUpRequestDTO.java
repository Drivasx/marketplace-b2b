package com.davidrivas.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CompanySignUpRequestDTO(
        @NotBlank(message = "Company name is required")
        String name,
        @NotBlank(message = "Company NIT is required")
        @Pattern(regexp = "^[0-9]+$", message = "Only numbers allowed")
        String nit,
        @NotBlank(message = "Plan is required")
        String plan,
        @NotBlank(message = "Currency is required")
        String currency,
        @NotBlank(message = "Language is required")
        String language
) {
}
