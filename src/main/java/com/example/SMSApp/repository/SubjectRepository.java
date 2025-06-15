package com.example.SMSApp.repository;

import com.example.SMSApp.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByPublicId(UUID publicId);
}
