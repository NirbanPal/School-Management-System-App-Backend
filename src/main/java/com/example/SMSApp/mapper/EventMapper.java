package com.example.SMSApp.mapper;


import com.example.SMSApp.dto.request.EventRequestDto;
import com.example.SMSApp.dto.response.EventResponseDto;
import com.example.SMSApp.model.ClassEntity;
import com.example.SMSApp.model.Event;

public class EventMapper {

    public static EventResponseDto toEventDto(Event event) {
        if (event == null) return null;

        return EventResponseDto.builder()
                .publicId(event.getPublicId())
                .title(event.getTitle())
                .description(event.getDescription())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .teacherId(event.getAnnouncer() != null ? event.getAnnouncer().getPublicId() : null)
                .teacherName(event.getAnnouncer() != null ? event.getAnnouncer().getPersonInfo().getFullName() : null)
                .classId(event.getClassEntity() != null ? event.getClassEntity().getPublicId() : null)
                .className(event.getClassEntity() != null ? event.getClassEntity().getName() : null)
                .build();

    }

    public static Event toEventEntity(EventRequestDto dto, Event existingEvent) {
        if (dto == null) return null;

        Event event = existingEvent != null ? existingEvent : new Event();

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());

        return event;
    }
}
