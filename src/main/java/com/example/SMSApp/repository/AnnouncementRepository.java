package com.example.SMSApp.repository;

import com.example.SMSApp.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {

    Optional<Announcement> findByPublicId(UUID publicId);

}
