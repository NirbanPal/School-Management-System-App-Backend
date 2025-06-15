package com.example.SMSApp.repository;

import com.example.SMSApp.model.Announcement;
import com.example.SMSApp.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {

    Optional<Announcement> findByPublicId(UUID publicId);

}
