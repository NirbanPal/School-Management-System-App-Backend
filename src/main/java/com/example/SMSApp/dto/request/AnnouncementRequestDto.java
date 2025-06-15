package com.example.SMSApp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class AnnouncementRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must not exceed 500 characters")
    private String description;

//    @NotNull(message = "Announcer ID is required")
    private UUID teacherId;

    // Optional: if announcements are sometimes not tied to a class, keep as nullable
    private UUID classId;
}
