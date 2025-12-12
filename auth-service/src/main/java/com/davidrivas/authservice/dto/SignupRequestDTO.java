package com.davidrivas.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid. e.g: test@example.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be valid")
        String password,


        @NotBlank(message = "Company name is required")
        String companyName,

        @NotBlank(message = "NIT is required")
        String nit,

        @NotBlank(message = "Plan is required")
        String plan,

        @NotBlank(message = "Currency is required")
        String currency,

        @NotBlank(message = "Language is required")
        String language
) {
}
