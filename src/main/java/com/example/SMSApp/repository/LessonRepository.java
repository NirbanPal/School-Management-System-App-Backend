package com.example.SMSApp.repository;

import com.example.SMSApp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson,Integer> {

    Optional<Lesson> findByPublicId(UUID publicId);
}
