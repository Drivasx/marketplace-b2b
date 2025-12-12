package com.davidrivas.authservice.grpc;

import com.davidrivas.authservice.dto.CompanySignUpRequestDTO;
import company.CompanyServiceGrpc;
import company.CreateCompanyRequest;
import company.CreateCompanyResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompanyServiceGrpcClient {

    private final CompanyServiceGrpc.CompanyServiceBlockingStub blockingStub;

    public CompanyServiceGrpcClient(
            @Value("${company.service.address:localhost}") String serverAddress,
            @Value("${company.service.grpc.port:9000}") int serverPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();
        this.blockingStub = CompanyServiceGrpc.newBlockingStub(channel);
    }

    public UUID createCompany(String name, String nit, String plan, String currency, String language, UUID userId) {
        CreateCompanyRequest request = CreateCompanyRequest.newBuilder()
                .setName(name)
                .setNit(nit)
                .setPlan(plan)
                .setCurrency(currency)
                .setLanguage(language)
                .setOwnerUserId(userId.toString())
                .build();

        CreateCompanyResponse response = blockingStub.createCompany(request);
        return UUID.fromString(response.getId());
    }

}
