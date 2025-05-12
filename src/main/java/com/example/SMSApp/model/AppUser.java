package com.example.SMSApp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
//    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @PrePersist
    public void assignPublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
        this.createdAt = LocalDateTime.now();
    }

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
}

