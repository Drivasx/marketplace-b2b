package com.davidrivas.authservice.service;

import com.davidrivas.authservice.dto.LoginRequestDTO;
import com.davidrivas.authservice.dto.LoginResponseDTO;
import com.davidrivas.authservice.dto.RefreshTokenRequestDTO;
import com.davidrivas.authservice.exception.UserNotFoundException;
import com.davidrivas.authservice.model.User;
import com.davidrivas.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        Optional<String> token = userService.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map( user -> jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getTenantId().toString()));

        Optional<String> refreshToken = userService.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .map( user -> jwtUtil.generateRefreshToken(user.getEmail(), user.getRole(), user.getTenantId().toString()));

        return new LoginResponseDTO(token.orElse(null), refreshToken.orElse(null));
    }

    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequest) {
        String refreshedToken = refreshTokenRequest.refreshToken();
        String email = jwtUtil.extractEmail(refreshedToken);

        User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(validateToken(refreshedToken)){
            String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getTenantId().toString());
            return new LoginResponseDTO(newAccessToken, refreshedToken);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try{
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
