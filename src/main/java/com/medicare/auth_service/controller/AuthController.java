package com.medicare.auth_service.controller;

import com.medicare.auth_service.dto.request.LoginRequest;
import com.medicare.auth_service.dto.request.RegisterRequest;
import com.medicare.auth_service.dto.response.TokenResponse;
import com.medicare.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req.username(), req.password(), req.email());
        return ResponseEntity.ok().build();
    }
}