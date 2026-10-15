package com.medicare.auth_service.config;

import com.medicare.auth_service.entity.Role;
import com.medicare.auth_service.entity.User;
import com.medicare.auth_service.enums.UserStatus;
import com.medicare.auth_service.repository.RoleRepository;
import com.medicare.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-admin.username}")
    private String adminUsername;

    @Value("${app.default-admin.password}")
    private String adminPassword;

    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        createRoleIfNotFound("ADMIN", "System Administrator");
        createRoleIfNotFound("INVENTORY_OPERATOR", "Manages inventory stock");
        createRoleIfNotFound("VIEWER", "Read-only access");

        if (userRepository.findByNormalizedUsername(adminUsername.toUpperCase()).isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();

            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setNormalizedUsername(adminUsername.toUpperCase());
            admin.setEmail("admin@medicare.internal");
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setStatus(UserStatus.ACTIVE);
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
            System.out.println("Default Admin user created successfully.");
        }
    }

    private void createRoleIfNotFound(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}