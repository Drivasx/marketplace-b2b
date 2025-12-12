package com.davidrivas.companyservice.controller;

import com.davidrivas.companyservice.dto.CompanyResponseDTO;
import com.davidrivas.companyservice.dto.EditCompanyRequestDTO;
import com.davidrivas.companyservice.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PutMapping("/me")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@Valid @RequestBody EditCompanyRequestDTO request){
        return ResponseEntity.ok(companyService.updateCompany(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCompany(){
        companyService.deleteCompany();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<CompanyResponseDTO> getMyCompany(){
        return ResponseEntity.ok(companyService.getMyCompany());
    }
}
