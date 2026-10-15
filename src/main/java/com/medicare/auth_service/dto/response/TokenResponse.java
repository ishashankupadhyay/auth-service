package com.medicare.auth_service.dto.response;
public record TokenResponse(String accessToken, String refreshToken, String tokenType, long expiresIn) {}
