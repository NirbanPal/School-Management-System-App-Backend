package com.example.SMSApp.repository;

import com.example.SMSApp.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result,Long> {
    Optional<Result> findByPublicId(UUID publicId);
}
