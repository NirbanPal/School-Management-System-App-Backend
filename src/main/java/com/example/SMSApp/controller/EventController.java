package com.example.SMSApp.controller;


import com.example.SMSApp.dto.request.EventRequestDto;
import com.example.SMSApp.dto.response.EventResponseDto;
import com.example.SMSApp.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvent());
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
        log.info("Creating event controller working");
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(eventRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable UUID id,
            @RequestBody @Valid EventRequestDto dto) {
        log.info("Updating event with id {}", id);
        return ResponseEntity.ok(eventService.updateEvent(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
