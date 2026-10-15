package com.medicare.auth_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", unique = true, nullable = false)
    private String tokenHash;

    @Column(name = "expiration_timestamp", nullable = false)
    private LocalDateTime expirationTimestamp;

    @Column(name = "revocation_timestamp")
    private LocalDateTime revocationTimestamp;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private LocalDateTime creationTimestamp;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by")
    private RefreshToken replacedBy;

    @PrePersist protected void onCreate() { creationTimestamp = LocalDateTime.now(); }

    // FIX: Explicit Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public LocalDateTime getExpirationTimestamp() { return expirationTimestamp; }
    public void setExpirationTimestamp(LocalDateTime expirationTimestamp) { this.expirationTimestamp = expirationTimestamp; }
    public LocalDateTime getRevocationTimestamp() { return revocationTimestamp; }
    public void setRevocationTimestamp(LocalDateTime revocationTimestamp) { this.revocationTimestamp = revocationTimestamp; }
    public LocalDateTime getCreationTimestamp() { return creationTimestamp; }
    public void setCreationTimestamp(LocalDateTime creationTimestamp) { this.creationTimestamp = creationTimestamp; }
    public RefreshToken getReplacedBy() { return replacedBy; }
    public void setReplacedBy(RefreshToken replacedBy) { this.replacedBy = replacedBy; }
}