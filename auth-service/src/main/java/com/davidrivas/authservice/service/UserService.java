package com.davidrivas.authservice.service;

import com.davidrivas.authservice.dto.CompanySignUpRequestDTO;
import com.davidrivas.authservice.dto.SignupRequestDTO;
import com.davidrivas.authservice.dto.SignupResponseDTO;
import com.davidrivas.authservice.grpc.CompanyServiceGrpcClient;
import com.davidrivas.authservice.mapper.UserMapper;
import com.davidrivas.authservice.model.User;
import com.davidrivas.authservice.repository.UserRepository;
import com.davidrivas.authservice.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final CompanyServiceGrpcClient companyServiceGrpcClient;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public SignupResponseDTO signupCompany(SignupRequestDTO request) {

        User user = new  User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_ADMIN");

        User savedUSer = userRepository.save(user);

        UUID tenantId = companyServiceGrpcClient.createCompany(request.companyName(), request.nit(), request.plan(), request.currency(), request.language(), savedUSer.getId());
        user.setTenantId(tenantId);

        String token = jwtUtil.generateToken(savedUSer.getEmail(), savedUSer.getRole(), savedUSer.getTenantId().toString());

        String refreshToken = jwtUtil.generateRefreshToken(savedUSer.getEmail(), savedUSer.getRole(), savedUSer.getTenantId().toString());

        userRepository.save(savedUSer);


        return UserMapper.toDTO(userRepository.save(user), token, refreshToken);


    }
}
