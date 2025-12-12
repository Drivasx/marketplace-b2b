package com.davidrivas.companyservice.dto;

import com.davidrivas.companyservice.model.Company;

public record CompanyResponseDTO(
        String id,
        String name,
        String nit,
        String plan,
        SettingsResponseDTO settings,
        String ownerUserId,
        String createdAt
)
{
    public record SettingsResponseDTO(
            String currency,
            String language
    ){}
}
