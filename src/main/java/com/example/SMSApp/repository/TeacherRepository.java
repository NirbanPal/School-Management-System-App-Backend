package com.example.SMSApp.repository;

import com.example.SMSApp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByPublicId(UUID publicId);
}
