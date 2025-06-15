package com.example.SMSApp.service;

import com.example.SMSApp.dto.request.EventRequestDto;
import com.example.SMSApp.dto.response.EventResponseDto;

import java.util.List;
import java.util.UUID;

public interface EventService {

    List<EventResponseDto> getAllEvent();

    EventResponseDto getEventById(UUID publicId);

    EventResponseDto createEvent(EventRequestDto eventRequestDto);

    EventResponseDto updateEvent(UUID id, EventRequestDto dto);

    void deleteEvent(UUID publicId);
}
