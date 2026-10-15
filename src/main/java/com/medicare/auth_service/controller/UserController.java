package com.medicare.auth_service.controller;

import com.medicare.auth_service.entity.Role;
import com.medicare.auth_service.entity.User;
import com.medicare.auth_service.repository.RoleRepository;
import com.medicare.auth_service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/{userId}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRole(@PathVariable UUID userId, @PathVariable String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok("Role " + role.getName() + " assigned to user " + user.getUsername());
    }
}