package com.example.SMSApp.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementResponseDto  {

    private UUID publicId;

    private String title;

    private String description;

    private LocalDateTime date;

    private UUID teacherId;
    private String teacherName; // Optional: Full name or username from AppUser

    private UUID classId;
    private String className; // Optional: For easy display in UI


}
