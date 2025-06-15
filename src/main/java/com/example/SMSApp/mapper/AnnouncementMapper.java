package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.AnnouncementRequestDto;
import com.example.SMSApp.dto.response.AnnouncementResponseDto;
import com.example.SMSApp.model.Announcement;

public class AnnouncementMapper {


    public static AnnouncementResponseDto toAnnouncementDto(Announcement announcement) {
        if (announcement == null) return null;


        return AnnouncementResponseDto.builder()
                .publicId(announcement.getPublicId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .teacherId(announcement.getAnnouncer() != null ? announcement.getAnnouncer().getPublicId() : null)
                .teacherName(announcement.getAnnouncer() != null && announcement.getAnnouncer().getPersonInfo() != null
                        ? announcement.getAnnouncer().getPersonInfo().getFullName() : null)
                .classId(announcement.getClassEntity() != null ? announcement.getClassEntity().getPublicId() : null)
                .className(announcement.getClassEntity() != null ? announcement.getClassEntity().getName() : null)
                .build();
    }

    public static Announcement toAnnouncementEntity(AnnouncementRequestDto dto, Announcement existingAnnouncement) {
        if (dto == null) return null;

        Announcement announcement = existingAnnouncement != null ? existingAnnouncement : new Announcement();

        announcement.setTitle(dto.getTitle());
        announcement.setDescription(dto.getDescription());

        return announcement;

    }
}
