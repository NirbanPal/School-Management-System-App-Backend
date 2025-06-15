package com.example.SMSApp.repository;

import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.Parent;
import com.example.SMSApp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByPublicId(UUID publicId);
    Optional<Student> findByAppUser(AppUser appUser);
}
