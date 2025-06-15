package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.AnnouncementRequestDto;
import com.example.SMSApp.dto.response.AnnouncementResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.AnnouncementMapper;
import com.example.SMSApp.model.Announcement;
import com.example.SMSApp.model.ClassEntity;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.AnnouncementRepository;
import com.example.SMSApp.repository.ClassRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    private final TeacherRepository teacherRepository;

    private final ClassRepository classRepository;

    @Override
    public List<AnnouncementResponseDto> getAllAnnouncements() {
        return announcementRepository.findAll()
                .stream()
                .map(AnnouncementMapper::toAnnouncementDto)
                .collect(Collectors.toList());
    }

    @Override
    public AnnouncementResponseDto createAnnouncement(AnnouncementRequestDto announcementRequestDto) {

        Announcement announcement = AnnouncementMapper.toAnnouncementEntity(announcementRequestDto,null);

        ClassEntity classEntity=null;
        if(announcementRequestDto.getClassId()!=null){
            classEntity = classRepository.findByPublicId(announcementRequestDto.getClassId()).orElseThrow(()->new ResourceNotFoundException("Class Not Found Exception."));
            classEntity.addAnnouncement(announcement);

        }

        Teacher teacher=null;
        if(announcementRequestDto.getTeacherId()!=null){
            teacher=teacherRepository.findByPublicId(announcementRequestDto.getTeacherId()).orElseThrow(()-> new ResourceNotFoundException("Teacher Not Found Exception."));
            announcement.setAnnouncer(teacher);
        }

        Announcement saved = announcementRepository.save(announcement);

        return AnnouncementMapper.toAnnouncementDto(saved);

    }

    @Override
    public AnnouncementResponseDto updateAnnouncement(UUID id, AnnouncementRequestDto dto) {

        Announcement exisitngAnnouncement = announcementRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Announcement Not Found."));

        exisitngAnnouncement = AnnouncementMapper.toAnnouncementEntity(dto,exisitngAnnouncement);

//        Update optional class association
        if (dto.getClassId() != null) {
            ClassEntity newClass = classRepository.findByPublicId(dto.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found."));

            // Update class only if changed
            ClassEntity oldClass = exisitngAnnouncement.getClassEntity();
            if (oldClass == null || !oldClass.getPublicId().equals(newClass.getPublicId())) {
                if (oldClass != null) {
                    oldClass.removeAnnouncement(exisitngAnnouncement); // maintain bidirectional sync
                }
                newClass.addAnnouncement(exisitngAnnouncement);
            }
        } else {
            // Remove existing class association if null is passed
            if (exisitngAnnouncement.getClassEntity() != null) {
                exisitngAnnouncement.getClassEntity().removeAnnouncement(exisitngAnnouncement);
            }
        }

//        Update optional teacher association
        if (dto.getTeacherId() != null) {
            Teacher newTeacher = teacherRepository.findByPublicId(dto.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found."));

            // Update only if changed
            Teacher currentTeacher = exisitngAnnouncement.getAnnouncer();
            if (currentTeacher == null || !currentTeacher.getPublicId().equals(newTeacher.getPublicId())) {
                exisitngAnnouncement.setAnnouncer(newTeacher);
            }
        } else {
            exisitngAnnouncement.setAnnouncer(null);
        }

        Announcement updated = announcementRepository.save(exisitngAnnouncement);

        return AnnouncementMapper.toAnnouncementDto(updated);
    }

    @Override
    public AnnouncementResponseDto getAnnouncementById(UUID id) {
        Announcement getAnnouncement = announcementRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Announcement Not Found."));

        return AnnouncementMapper.toAnnouncementDto(getAnnouncement);
    }

    @Override
    public void deleteAnnouncement(UUID id) {
        Announcement deletedAnnouncement = announcementRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Announcement Not Found."));

        if(deletedAnnouncement.getAnnouncer()!=null){
            deletedAnnouncement.setAnnouncer(null);
        }

        ClassEntity classEntity=deletedAnnouncement.getClassEntity();
        if(classEntity!=null){
            classEntity.removeAnnouncement(deletedAnnouncement);
        }

        announcementRepository.delete(deletedAnnouncement);

    }
}
