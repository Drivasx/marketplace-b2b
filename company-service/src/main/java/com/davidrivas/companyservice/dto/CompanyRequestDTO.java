package com.davidrivas.companyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CompanyRequestDTO(
        @NotBlank(message = "Company name is required")
        String name,
        @NotBlank(message = "Company NIT is required")
        @Pattern(regexp = "^[0-9]+$", message = "Only numbers allowed")
        String nit,
        @NotBlank(message = "Plan is required")
        String plan,
        @NotNull(message = "Company currency are required")
        SettingRequestDTO settings
) {
    public record SettingRequestDTO(
            @NotBlank(message = "Currency is required")
            String currency,
            @NotBlank(message = "Language is required")
            String language) {}
}
