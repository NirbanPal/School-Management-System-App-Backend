package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.ResultRequestDto;
import com.example.SMSApp.dto.response.ResultResponseDto;
import com.example.SMSApp.exception.custom.BadRequestException;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.ResultMapper;
import com.example.SMSApp.model.*;
import com.example.SMSApp.repository.*;

import com.example.SMSApp.repository.ResultRepository;
import com.example.SMSApp.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    private final ExamRepository examRepository;

    private final AssignmentRepository assignmentRepository;

    private final StudentRepository studentRepository;


    @Override
    public List<ResultResponseDto> getAllResults() {

        return resultRepository.findAll()
                .stream()
                .map(ResultMapper::toResultDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResultResponseDto createResult(ResultRequestDto resultRequestDto) {

        Student assignedStudent = studentRepository.findByPublicId(resultRequestDto.getStudentId()).orElseThrow(()-> new ResourceNotFoundException("Student not found."));

        // Ensure only one of examId or assignmentId is provided
        boolean hasExam = resultRequestDto.getExamId() != null;
        boolean hasAssignment = resultRequestDto.getAssignmentId() != null;

        if (hasExam == hasAssignment) {
            throw new BadRequestException("Exactly one of examId or assignmentId must be provided.");
        }

        // Map to entity and persist
        Result result = ResultMapper.toResultEntity(resultRequestDto,null);

        if (hasExam) {
            Exam exam = examRepository.findByPublicId(resultRequestDto.getExamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exam not found."));
            exam.addResult(result);
        } else {
            Assignment assignment = assignmentRepository.findByPublicId(resultRequestDto.getAssignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignment not found."));
            assignment.addResult(result);
        }

        assignedStudent.addResult(result);

        Result savedResult = resultRepository.save(result);

        // Return DTO
        return ResultMapper.toResultDto(savedResult);

    }

    @Override
    public ResultResponseDto updateResult(UUID id, ResultRequestDto dto) {

        Result existingResult = resultRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found."));

        Student newAssignedStudent = studentRepository.findByPublicId(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found."));

        boolean hasExam = dto.getExamId() != null;
        boolean hasAssignment = dto.getAssignmentId() != null;

        if (hasExam == hasAssignment) {
            throw new BadRequestException("Exactly one of examId or assignmentId must be provided.");
        }

        Exam oldExam = existingResult.getExam();
        Assignment oldAssignment = existingResult.getAssignment();
        Student oldStudent = existingResult.getStudent();

        existingResult=ResultMapper.toResultEntity(dto,existingResult);

        if (hasExam) {

            Exam newAssignedExam = examRepository.findByPublicId(dto.getExamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exam not found."));

            if(oldExam != null && !Objects.equals(oldExam.getId(), newAssignedExam.getId())){
                oldExam.removeResult(existingResult);
                newAssignedExam.addResult(existingResult);
            }
        } else {

            Assignment newAssignedAssignment = assignmentRepository.findByPublicId(dto.getAssignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignment not found."));

            if( oldAssignment != null && !Objects.equals(oldAssignment.getId(), newAssignedAssignment.getId())){
                oldAssignment.removeResult(existingResult);
                newAssignedAssignment.addResult(existingResult);
            }
        }

        if(oldStudent != null && !Objects.equals(oldStudent.getId(), newAssignedStudent.getId())){
            oldStudent.removeResult(existingResult);
            newAssignedStudent.addResult(existingResult);
        }


        Result updated = resultRepository.save(existingResult);
        return ResultMapper.toResultDto(updated);
    }

    @Override
    public ResultResponseDto getResultById(UUID id) {
        Result getResult = resultRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found."));
        return ResultMapper.toResultDto(getResult);
    }

    @Override
    public void deleteResult(UUID id) {
        Result deletedResult = resultRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found."));

        Exam exam=deletedResult.getExam();
        if(exam!=null){
            exam.removeResult(deletedResult);
        }

        Assignment assignment = deletedResult.getAssignment();
        if(assignment!=null){
            assignment.removeResult(deletedResult);
        }

        Student student=deletedResult.getStudent();
        if(student!=null){
            student.removeResult(deletedResult);
        }

        resultRepository.delete(deletedResult);
    }
}
