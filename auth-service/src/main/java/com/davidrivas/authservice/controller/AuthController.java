package com.davidrivas.authservice.controller;

import com.davidrivas.authservice.dto.LoginRequestDTO;
import com.davidrivas.authservice.dto.LoginResponseDTO;
import com.davidrivas.authservice.dto.RefreshTokenRequestDTO;
import com.davidrivas.authservice.dto.TokenValidationResponse;
import com.davidrivas.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication and authorization")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login existent user")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDTO));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate if generated token is valid")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return authService.validateToken(authHeader.substring(7)) != null ?
                  ResponseEntity.ok(authService.validateToken(authHeader.substring(7)))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token with longer duration based on first token provided")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        if(authService.refreshToken(request) == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
