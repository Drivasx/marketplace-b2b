package com.davidrivas.authservice.service;

import com.davidrivas.authservice.dto.LoginRequestDTO;
import com.davidrivas.authservice.dto.LoginResponseDTO;
import com.davidrivas.authservice.dto.RefreshTokenRequestDTO;
import com.davidrivas.authservice.dto.TokenValidationResponse;
import com.davidrivas.authservice.exception.UserNotFoundException;
import com.davidrivas.authservice.exception.WrongCredentialsException;
import com.davidrivas.authservice.model.User;
import com.davidrivas.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        Optional<String> optionalToken = userService.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map( user -> jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getTenantId().toString()));

        Optional<String> optionalRefreshToken = userService.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map( user -> jwtUtil.generateRefreshToken(user.getEmail(), user.getRole(), user.getTenantId().toString()));

        String token = optionalToken.orElseThrow(() -> new WrongCredentialsException("Unauthorized"));
        String refreshToken = optionalRefreshToken.orElseThrow(() -> new WrongCredentialsException("Unauthorized"));

        return new LoginResponseDTO(token, refreshToken);
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest) {
        String refreshedToken = refreshTokenRequest.refreshToken();
        String email = jwtUtil.extractEmail(refreshedToken);

        User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(validateToken(refreshedToken).valid()){
            String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getTenantId().toString());
            return new LoginResponseDTO(newAccessToken, refreshedToken);
        }
        return null;
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            boolean isValid = jwtUtil.validateToken(token);
            String tenantId = jwtUtil.extractTenantId(token);
            String userId = userService.findByEmail(jwtUtil.extractEmail(token)).get().getId().toString();
            List<String> roles = jwtUtil.extractRoles(token);

            return new TokenValidationResponse(
                    isValid,
                    userId,
                    tenantId,
                    roles);
        } catch (JwtException e) {
            return null;
        }
    }
}
