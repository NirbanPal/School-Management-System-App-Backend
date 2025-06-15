package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.ExamRequestDto;
import com.example.SMSApp.dto.response.ExamResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.ClassMapper;
import com.example.SMSApp.mapper.ExamMapper;
import com.example.SMSApp.model.Lesson;
import com.example.SMSApp.model.Exam;
import com.example.SMSApp.model.Subject;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.ExamRepository;
import com.example.SMSApp.repository.LessonRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.ExamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final LessonRepository lessonRepository;

    private final TeacherRepository teacherRepository;


    @Override
    public List<ExamResponseDto> getAllExams() {
        return examRepository.findAll()
                .stream()
                .map(ExamMapper::toExamDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponseDto createExam(ExamRequestDto examRequestDto) {

        Lesson assignedLesson= lessonRepository.findByPublicId(examRequestDto.getLessonId()).orElseThrow(()->new ResourceNotFoundException("Lesson Not Found."));

        Teacher assignedTeacher = teacherRepository.findByPublicId(examRequestDto.getTakenByTeacherId()).orElseThrow(()->new ResourceNotFoundException("Teacher Not Found."));

        Exam exam=ExamMapper.toExamEntity(examRequestDto,null);

        assignedLesson.addExam(exam);
        exam.setTakenBy(assignedTeacher);

        Exam savedExam = examRepository.save(exam);

        return ExamMapper.toExamDto(savedExam);
    }

    @Override
    public ExamResponseDto updateExam(UUID id, ExamRequestDto dto) {

        Exam existingExam = examRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Exam Not Found."));

        Lesson newAssignedLesson= lessonRepository.findByPublicId(dto.getLessonId()).orElseThrow(()->new ResourceNotFoundException("Lesson Not Found."));

        Teacher newAssignedTeacher = teacherRepository.findByPublicId(dto.getTakenByTeacherId()).orElseThrow(()->new ResourceNotFoundException("Teacher Not Found."));

        Exam exam= ExamMapper.toExamEntity(dto,existingExam);

        Lesson oldLesson = exam.getLesson();
        if(!oldLesson.getId().equals(newAssignedLesson.getId())){
            oldLesson.removeExam(exam);
            newAssignedLesson.addExam(exam);
        }

        Teacher oldTeacher = exam.getTakenBy();
        if(!oldTeacher.getId().equals(newAssignedTeacher.getId())){
            exam.setTakenBy(newAssignedTeacher);
        }

        Exam saved=examRepository.save(exam);


        return ExamMapper.toExamDto(saved);
    }

    @Override
    public ExamResponseDto getExamById(UUID id) {
        Exam getExam = examRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Exam Not Found."));
        return ExamMapper.toExamDto(getExam);
    }

    @Override
    public void deleteExam(UUID id) {

        Exam deletedExam = examRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Exam Not Found."));

        deletedExam.setTakenBy(null);

        Lesson lesson=deletedExam.getLesson();
        if(lesson!=null){
            lesson.removeExam(deletedExam);
        }

        examRepository.delete(deletedExam);

    }
}
