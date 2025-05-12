package com.example.SMSApp.repository;

import com.example.SMSApp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPublicId(UUID publicId);
    boolean existsByEmail(String email);
}
