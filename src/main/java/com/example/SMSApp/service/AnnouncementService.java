package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.AnnouncementRequestDto;
import com.example.SMSApp.dto.response.AnnouncementResponseDto;

import java.util.List;
import java.util.UUID;

public interface AnnouncementService {

    List<AnnouncementResponseDto> getAllAnnouncements();

    AnnouncementResponseDto createAnnouncement(AnnouncementRequestDto assignmentRequestDto);

    AnnouncementResponseDto updateAnnouncement(UUID id, AnnouncementRequestDto dto);

    AnnouncementResponseDto getAnnouncementById(UUID id);

    void deleteAnnouncement(UUID id);
}
