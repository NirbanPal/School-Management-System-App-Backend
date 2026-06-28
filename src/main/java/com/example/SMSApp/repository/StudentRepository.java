package com.example.SMSApp.repository;

import com.example.SMSApp.entity.AppUser;
import com.example.SMSApp.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByPublicId(UUID publicId);
    Optional<Student> findByAppUser(AppUser appUser);
}
