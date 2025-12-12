package com.davidrivas.authservice.controller;

import com.davidrivas.authservice.dto.SignupRequestDTO;
import com.davidrivas.authservice.dto.SignupResponseDTO;
import com.davidrivas.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {
        return ResponseEntity.ok(userService.signupCompany(signupRequestDTO));
    }
}
