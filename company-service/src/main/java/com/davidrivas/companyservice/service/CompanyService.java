package com.davidrivas.companyservice.service;

import com.davidrivas.companyservice.dto.CompanyRequestDTO;
import com.davidrivas.companyservice.dto.CompanyResponseDTO;
import com.davidrivas.companyservice.dto.EditCompanyRequestDTO;
import com.davidrivas.companyservice.exception.CompanyAlreadyExistsException;
import com.davidrivas.companyservice.exception.CompanyNotFoundException;
import com.davidrivas.companyservice.mapper.CompanyMapper;
import com.davidrivas.companyservice.model.Company;
import com.davidrivas.companyservice.repository.CompanyRepository;
import com.davidrivas.companyservice.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;


    public CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO, String ownerUserId) {

        Optional<Company> existentCompanyByName = companyRepository.findByName(companyRequestDTO.name());

        Optional<Company> existentCompanyByNit = companyRepository.findByNit(companyRequestDTO.nit());

        if (existentCompanyByName.isPresent() ||  existentCompanyByNit.isPresent()) {
            throw new CompanyAlreadyExistsException("This company is already in the system");
        }

        Company company = CompanyMapper.toEntity(companyRequestDTO);
        company.setId(UUID.randomUUID());
        company.setOwnerUserId(UUID.fromString(ownerUserId));
        company.setCreatedAt(LocalDateTime.now().toString());
        companyRepository.save(company);

        return CompanyMapper.toDTO(company);
    }

    public CompanyResponseDTO updateCompany(EditCompanyRequestDTO request) {
        Company company = companyRepository.findById(TenantContext.getCurrentTenant())
                .orElseThrow(()-> new CompanyNotFoundException("This company doesn't exist"));

        company.setName(request.name());
        company.setPlan(request.plan());

        if(request.settings() != null) {
            Company.Settings settings = new Company.Settings();
            settings.setCurrency(request.settings().currency());
            settings.setLanguage(request.settings().language());

            company.setSettings(settings);
        }

        companyRepository.save(company);
        return CompanyMapper.toDTO(company);
    }

    public CompanyResponseDTO getMyCompany() {
        Company myCompany = companyRepository.findById(TenantContext.getCurrentTenant()).orElseThrow(()-> new CompanyNotFoundException("This company doesn't exist"));
        return CompanyMapper.toDTO(myCompany);
    }


    public void deleteCompany() {
        Company company = companyRepository.findById(TenantContext.getCurrentTenant()).orElseThrow(()-> new CompanyNotFoundException("This company doesn't exist"));
        companyRepository.delete(company);
    }

    public List<CompanyResponseDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(CompanyMapper::toDTO).toList();
    }


}
