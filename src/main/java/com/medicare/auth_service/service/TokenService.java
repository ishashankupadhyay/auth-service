package com.medicare.auth_service.service;

import com.medicare.auth_service.security.SecurityPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token.expiration-ms}")
    private long expirationMs;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateAccessToken(SecurityPrincipal principal) {
        Instant now = Instant.now();
        String scope = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusMillis(expirationMs))
                .subject(principal.getUser().getId().toString())
                .claim("scope", scope)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}