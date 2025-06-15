package com.example.SMSApp.repository;

import com.example.SMSApp.model.Attendance;
import com.example.SMSApp.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Optional<Attendance> findByPublicId(UUID publicId);

}
