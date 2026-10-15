package com.medicare.auth_service.service;

import com.medicare.auth_service.dto.request.LoginRequest;
import com.medicare.auth_service.dto.response.TokenResponse;
import com.medicare.auth_service.entity.User;
import com.medicare.auth_service.enums.UserStatus;
import com.medicare.auth_service.repository.RoleRepository;
import com.medicare.auth_service.repository.UserRepository;
import com.medicare.auth_service.security.SecurityPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authManager, TokenService tokenService,
                       RefreshTokenService refreshTokenService, UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public TokenResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        SecurityPrincipal principal = (SecurityPrincipal) auth.getPrincipal();
        User user = principal.getUser();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        String access = tokenService.generateAccessToken(principal);
        String refresh = refreshTokenService.createRefreshToken(user);
        return new TokenResponse(access, refresh, "Bearer", 900);
    }

    @Transactional
    public void register(String username, String rawPassword, String email) {
        if(userRepository.findByNormalizedUsername(username.toUpperCase()).isPresent()) {
            throw new RuntimeException("User exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setNormalizedUsername(username.toUpperCase());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setStatus(UserStatus.ACTIVE);
        roleRepository.findByName("VIEWER").ifPresent(r -> user.setRoles(Set.of(r)));
        userRepository.save(user);
    }
}