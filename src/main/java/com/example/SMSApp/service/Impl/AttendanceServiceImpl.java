package com.example.SMSApp.service.Impl;


import com.example.SMSApp.dto.request.AttendanceRequestDto;
import com.example.SMSApp.dto.response.AttendanceResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.AttendanceMapper;
import com.example.SMSApp.model.Attendance;
import com.example.SMSApp.model.Lesson;
import com.example.SMSApp.model.Student;
import com.example.SMSApp.repository.AttendanceRepository;
import com.example.SMSApp.repository.LessonRepository;
import com.example.SMSApp.repository.StudentRepository;
import com.example.SMSApp.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final StudentRepository studentRepository;

    private final LessonRepository lessonRepository;


    @Override
    public AttendanceResponseDto createAttendance(AttendanceRequestDto attendanceRequestDto) {

        Student student = studentRepository.findByPublicId(attendanceRequestDto.getStudentId()).orElseThrow(()->new ResourceNotFoundException("Student Not Found."));

        Lesson lesson = lessonRepository.findByPublicId(attendanceRequestDto.getLessonId()).orElseThrow(()-> new ResourceNotFoundException("Lesson Not Found."));

        Attendance attendance = AttendanceMapper.toAttendanceEntity(attendanceRequestDto,null);

        student.addAttendance(attendance);
        lesson.addAttendance(attendance);

        Attendance saved = attendanceRepository.save(attendance);


        return AttendanceMapper.toAttendanceDto(saved);
    }

    @Override
    public AttendanceResponseDto updateAttendance(UUID id, AttendanceRequestDto dto) {
        // Fetch existing attendance by publicId
        Attendance attendance = attendanceRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + id));

        // Fetch and validate associated student
        Student student = studentRepository.findByPublicId(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + dto.getStudentId()));

        // Fetch and validate associated lesson
        Lesson lesson = lessonRepository.findByPublicId(dto.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with ID: " + dto.getLessonId()));

        // Update fields
        attendance.setPresent(dto.getPresent());

        // Update associations only if changed
        if (!attendance.getStudent().getId().equals(student.getId())) {
            student.addAttendance(attendance);
        }

        if (!attendance.getLesson().getId().equals(lesson.getId())) {
            lesson.addAttendance(attendance);
        }

        // Save and return response DTO
        Attendance updated = attendanceRepository.save(attendance);
        return AttendanceMapper.toAttendanceDto(updated);
    }

    @Override
    public void deleteAttendance(UUID publicId) {

        Attendance deletedAttendance = attendanceRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with ID: " + publicId));

        Lesson lesson=deletedAttendance.getLesson();
        if(lesson!=null){
            lesson.removeAttendance(deletedAttendance);
        }


        Student student=deletedAttendance.getStudent();
        if(student!=null){
            student.removeAttendance(deletedAttendance);
        }

        attendanceRepository.delete(deletedAttendance);
    }
}
