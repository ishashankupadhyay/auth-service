package com.medicare.auth_service.repository;
import com.medicare.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByNormalizedUsername(String normalizedUsername);
}
