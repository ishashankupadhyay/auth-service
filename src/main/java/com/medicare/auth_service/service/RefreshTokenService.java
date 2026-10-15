package com.medicare.auth_service.service;

import com.medicare.auth_service.entity.RefreshToken;
import com.medicare.auth_service.entity.User;
import com.medicare.auth_service.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final PasswordEncoder encoder;

    @Value("${jwt.refresh-token.expiration-ms}")
    private long expirationMs;

    public RefreshTokenService(RefreshTokenRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public String createRefreshToken(User user) {
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(UUID.randomUUID().toString().getBytes());
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setTokenHash(encoder.encode(rawToken));
        rt.setExpirationTimestamp(LocalDateTime.now().plusNanos(expirationMs * 1000000));
        repository.save(rt);
        return rawToken;
    }
}