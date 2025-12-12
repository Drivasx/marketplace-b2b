package com.davidrivas.companyservice.repository;

import com.davidrivas.companyservice.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends MongoRepository<Company, UUID> {

    Optional<Company> findByName(String name);

    Optional<Company> findByNit(String nit);
}
