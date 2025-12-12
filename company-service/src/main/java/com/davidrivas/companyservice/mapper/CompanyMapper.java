package com.davidrivas.companyservice.mapper;

import com.davidrivas.companyservice.dto.CompanyRequestDTO;
import com.davidrivas.companyservice.dto.CompanyResponseDTO;
import com.davidrivas.companyservice.dto.EditCompanyRequestDTO;
import com.davidrivas.companyservice.model.Company;


public class CompanyMapper {

    public static Company toEntity(CompanyRequestDTO request){
        Company company = new Company();
        company.setName(request.name());
        company.setNit(request.nit());
        company.setPlan(request.plan());

        if(request.settings() != null){
            Company.Settings settings = new Company.Settings();
            settings.setCurrency(request.settings().currency());
            settings.setLanguage(request.settings().language());
            company.setSettings(settings);
        }

        return company;
    }

    public static Company editToEntity(EditCompanyRequestDTO request){
        Company company = new Company();
        company.setName(request.name());
        company.setPlan(request.plan());

        if(request.settings() != null){
            Company.Settings settings = new Company.Settings();
            settings.setCurrency(request.settings().currency());
            settings.setLanguage(request.settings().language());
            company.setSettings(settings);
        }

        return company;
    }

    public static CompanyResponseDTO toDTO(Company company){
        CompanyResponseDTO.SettingsResponseDTO settingsDTO = null;
        if (company.getSettings() != null) {
            settingsDTO = new CompanyResponseDTO.SettingsResponseDTO(
                    company.getSettings().getCurrency(),
                    company.getSettings().getLanguage()
            );
        }

        return new CompanyResponseDTO(
                company.getId().toString(),
                company.getName(),
                company.getNit(),
                company.getPlan(),
                settingsDTO,
                company.getOwnerUserId() != null ? company.getOwnerUserId().toString() : null,
                company.getCreatedAt() != null ? company.getCreatedAt() : null
        );
    }
}
