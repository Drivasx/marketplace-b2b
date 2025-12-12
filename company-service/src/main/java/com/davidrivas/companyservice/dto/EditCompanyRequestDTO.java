package com.davidrivas.companyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditCompanyRequestDTO(
        @NotBlank(message = "Company name is required")
        String name,
        @NotBlank(message = "Plan is required")
        String plan,
        @NotNull(message = "Company currency are required")
        CompanyRequestDTO.SettingRequestDTO settings
) {
    public record SettingRequestDTO(
            @NotBlank(message = "Currency is required")
            String currency,
            @NotBlank(message = "Language is required")
            String language) {}
}
