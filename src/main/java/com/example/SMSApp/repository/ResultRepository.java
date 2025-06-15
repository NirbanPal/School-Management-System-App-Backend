package com.example.SMSApp.repository;

import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Parent;
import com.example.SMSApp.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result,Long> {
    Optional<Result> findByPublicId(UUID publicId);
}
