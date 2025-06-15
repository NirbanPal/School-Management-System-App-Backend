package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.EventRequestDto;
import com.example.SMSApp.dto.response.EventResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.EventMapper;
import com.example.SMSApp.model.ClassEntity;
import com.example.SMSApp.model.Event;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.ClassRepository;
import com.example.SMSApp.repository.EventRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public final TeacherRepository teacherRepository;

    public final ClassRepository classRepository;

    @Override
    public List<EventResponseDto> getAllEvent() {
        return eventRepository.findAll()
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventResponseDto getEventById(UUID publicId) {
        Event getEvent = eventRepository.findByPublicId(publicId).orElseThrow(()-> new ResourceNotFoundException("Even Not Found."));
        return EventMapper.toEventDto(getEvent);
    }

    @Override
    public EventResponseDto createEvent(EventRequestDto dto) {

        Event event= EventMapper.toEventEntity(dto,null);

        if(dto.getTeacherId()!=null){
            Teacher teacher = teacherRepository.findByPublicId(dto.getTeacherId()).orElseThrow(()-> new ResourceNotFoundException("Teacher Not Found."));
            event.setAnnouncer(teacher);
        }

        if(dto.getClassId()!=null){
            ClassEntity classEntity = classRepository.findByPublicId(dto.getClassId()).orElseThrow(()-> new ResourceNotFoundException("Class Not Found."));
            classEntity.addEvent(event);
        }

        Event saved  = eventRepository.save(event);

        return EventMapper.toEventDto(saved);
    }

    @Override
    public EventResponseDto updateEvent(UUID id, EventRequestDto dto) {

        Event existingEvent = eventRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found."));

        existingEvent = EventMapper.toEventEntity(dto,existingEvent);

        // === Update Teacher association ===
        Teacher newTeacher = null;
        if (dto.getTeacherId() != null) {
            newTeacher = teacherRepository.findByPublicId(dto.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found."));
        }
        existingEvent.setAnnouncer(newTeacher); // can be null (nullable)

        // === Update Class association ===
        ClassEntity newClassEntity = null;
        if (dto.getClassId() != null) {
            newClassEntity = classRepository.findByPublicId(dto.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found."));
            newClassEntity.addEvent(existingEvent); // maintain bidirectional
        } else {
            // If classId is null, disassociate from the current class
            ClassEntity oldClassEntity = existingEvent.getClassEntity();
            if (oldClassEntity != null) {
                oldClassEntity.removeEvent(existingEvent); // if implemented
            }
        }

        Event updated = eventRepository.save(existingEvent);
        return EventMapper.toEventDto(updated);
    }

    @Override
    public void deleteEvent(UUID publicId) {

        Event deletedEvent = eventRepository.findByPublicId(publicId).orElseThrow(()-> new ResourceNotFoundException("Even Not Found."));

        ClassEntity classDeleted=deletedEvent.getClassEntity();
        if(classDeleted!=null){
            classDeleted.removeEvent(deletedEvent);
        }

        if(deletedEvent.getAnnouncer()!=null){
            deletedEvent.setAnnouncer(null);
        }

        eventRepository.delete(deletedEvent);
    }
}
