package com.example.SMSApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    // Primary key using auto-increment strategy
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Public-facing UUID identifier, immutable and unique
    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    // Timestamp for creation date, set once
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Set default values before persisting to the database
    @PrePersist
    public void prePersist() {
        if (publicId == null) publicId = UUID.randomUUID();
    }
}
