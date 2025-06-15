package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.AnnouncementRequestDto;
import com.example.SMSApp.dto.response.AnnouncementResponseDto;
import com.example.SMSApp.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/announcement")
@RequiredArgsConstructor
@Slf4j
public class AnnouncementController {
    private final AnnouncementService assignmentService;

    @GetMapping
    public ResponseEntity<List<AnnouncementResponseDto>> getAllAnnouncements() {
        return ResponseEntity.ok(assignmentService.getAllAnnouncements());
    }

    @PostMapping
    public ResponseEntity<AnnouncementResponseDto> createAnnouncement(@RequestBody @Valid AnnouncementRequestDto assignmentRequestDto) {
        log.info("Creating assignment controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.createAnnouncement(assignmentRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementResponseDto> updateAnnouncement(
            @PathVariable UUID id,
            @RequestBody @Valid AnnouncementRequestDto dto) {
        log.info("Updating assignment with id {}", id);
        return ResponseEntity.ok(assignmentService.updateAnnouncement(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponseDto> getAnnouncementById(@PathVariable UUID id) {
        return ResponseEntity.ok(assignmentService.getAnnouncementById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable UUID id) {
        assignmentService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
}
