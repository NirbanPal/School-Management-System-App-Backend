package com.example.SMSApp.repository;

import com.example.SMSApp.model.Exam;
import com.example.SMSApp.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepository extends JpaRepository<Exam,Long> {
    Optional<Exam> findByPublicId(UUID publicId);

}
