package com.example.SMSApp.repository;

import com.example.SMSApp.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment,Long> {

    Optional<Assignment> findByPublicId(UUID publicId);

}
