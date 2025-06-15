package com.example.SMSApp.repository;

import com.example.SMSApp.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity,Long> {
    Optional<ClassEntity> findByPublicId(UUID publicId);
}
