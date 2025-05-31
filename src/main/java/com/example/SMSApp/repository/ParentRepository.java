package com.example.SMSApp.repository;

import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent,Long> {
    Optional<Parent> findByPublicId(UUID publicId);
    Optional<Parent> findByAppUser(AppUser appUser);
}
