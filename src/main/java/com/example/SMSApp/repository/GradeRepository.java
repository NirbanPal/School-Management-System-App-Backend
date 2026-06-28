package com.example.SMSApp.repository;

import com.example.SMSApp.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByPublicId(UUID publicId);
}
