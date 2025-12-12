package com.davidrivas.companyservice.grpc;

import com.davidrivas.companyservice.dto.CompanyRequestDTO;
import com.davidrivas.companyservice.dto.CompanyResponseDTO;
import com.davidrivas.companyservice.model.Company;
import com.davidrivas.companyservice.service.CompanyService;
import company.CreateCompanyRequest;
import company.CreateCompanyResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import company.CompanyServiceGrpc;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CompanyGrpcService extends CompanyServiceGrpc.CompanyServiceImplBase {

    private final CompanyService companyService;

    @Override
    public void createCompany(CreateCompanyRequest request, StreamObserver<CreateCompanyResponse> responseObserver) {
        log.info("Received request to Create Company: {}", request);

        CompanyResponseDTO company = companyService.createCompany(new CompanyRequestDTO(
                request.getName(),
                request.getNit(),
                request.getPlan(),
                new CompanyRequestDTO.SettingRequestDTO(
                        request.getCurrency(),
                        request.getLanguage()
                )
        ), request.getOwnerUserId());
        company.

        CreateCompanyResponse response = CreateCompanyResponse.newBuilder()
                .setId(company.id())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
