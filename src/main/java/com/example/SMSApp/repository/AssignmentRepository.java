package com.example.SMSApp.repository;

import com.example.SMSApp.model.Assignment;
import com.example.SMSApp.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

    Optional<Assignment> findByPublicId(UUID publicId);

}
