package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.AssignmentRequestDto;
import com.example.SMSApp.dto.response.AssignmentResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.AssignmentMapper;
import com.example.SMSApp.model.*;
import com.example.SMSApp.model.Assignment;
import com.example.SMSApp.repository.AssignmentRepository;
import com.example.SMSApp.repository.LessonRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final LessonRepository lessonRepository;

    private final TeacherRepository teacherRepository;

    @Override
    public List<AssignmentResponseDto> getAllAssignments() {
        return assignmentRepository.findAll()
                .stream()
                .map(AssignmentMapper::toAssignmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentResponseDto createAssignment(AssignmentRequestDto assignmentRequestDto) {

        Lesson assignedLesson= lessonRepository.findByPublicId(assignmentRequestDto.getLessonId()).orElseThrow(()->new ResourceNotFoundException("Lesson Not Found."));

        Teacher assignedTeacher = teacherRepository.findByPublicId(assignmentRequestDto.getTakenByTeacherId()).orElseThrow(()->new ResourceNotFoundException("Teacher Not Found."));

        Assignment assignment= AssignmentMapper.toAssignmentEntity(assignmentRequestDto,null);

        assignedLesson.addAssignment(assignment);
        assignment.setTakenBy(assignedTeacher);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return AssignmentMapper.toAssignmentDto(savedAssignment);
    }

    @Override
    public AssignmentResponseDto updateAssignment(UUID id, AssignmentRequestDto dto) {
        Assignment existingAssignment = assignmentRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Assignment Not Found."));

        Lesson newAssignedLesson= lessonRepository.findByPublicId(dto.getLessonId()).orElseThrow(()->new ResourceNotFoundException("Lesson Not Found."));

        Teacher newAssignedTeacher = teacherRepository.findByPublicId(dto.getTakenByTeacherId()).orElseThrow(()->new ResourceNotFoundException("Teacher Not Found."));

        Assignment assignment= AssignmentMapper.toAssignmentEntity(dto,existingAssignment);

        Lesson oldLesson = assignment.getLesson();
        if(!oldLesson.getId().equals(newAssignedLesson.getId())){
            oldLesson.removeAssignment(assignment);
            newAssignedLesson.addAssignment(assignment);
        }

        Teacher oldTeacher = assignment.getTakenBy();
        if(!oldTeacher.getId().equals(newAssignedTeacher.getId())){
            assignment.setTakenBy(newAssignedTeacher);
        }

        Assignment saved=assignmentRepository.save(assignment);

        return AssignmentMapper.toAssignmentDto(saved);
    }

    @Override
    public AssignmentResponseDto getAssignmentById(UUID id) {
        Assignment getAssignment = assignmentRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Assignment Not Found."));
        return AssignmentMapper.toAssignmentDto(getAssignment);
    }

    @Override
    public void deleteAssignment(UUID id) {

        Assignment deletedAssignment = assignmentRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Assignment Not Found."));

        deletedAssignment.setTakenBy(null);

        Lesson lesson=deletedAssignment.getLesson();
        if(lesson!=null){
            lesson.removeAssignment(deletedAssignment);
        }

        assignmentRepository.delete(deletedAssignment);

    }
}
