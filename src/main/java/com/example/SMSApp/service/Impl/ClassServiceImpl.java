package com.example.SMSApp.service.Impl;


import com.example.SMSApp.dto.request.ClassRequestDto;
import com.example.SMSApp.dto.response.ClassResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.ClassMapper;
import com.example.SMSApp.model.ClassEntity;
import com.example.SMSApp.model.Grade;
import com.example.SMSApp.model.Parent;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.ClassRepository;
import com.example.SMSApp.repository.GradeRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.ClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    private final TeacherRepository teacherRepository;

    private final GradeRepository gradeRepository;

    @Override
    public List<ClassResponseDto> getAllClass() {

        return classRepository.findAll()
                .stream()
                .map(ClassMapper::toClassDto)
                .collect(Collectors.toList());
    }
    @Override
    public ClassResponseDto getClassById(UUID publicId) {
        ClassEntity classFetch=classRepository.findByPublicId(publicId).orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        return ClassMapper.toClassDto(classFetch);
    }

    @Override
    public ClassResponseDto createClass(ClassRequestDto classRequestDto) {


        Teacher assignedTeacher= teacherRepository.findByPublicId(classRequestDto.getTeacherId()).orElseThrow(()-> new ResourceNotFoundException("Teacher not found"));

        Grade assignedGrade = gradeRepository.findByPublicId(classRequestDto.getGradeId()).orElseThrow(()-> new ResourceNotFoundException("Grade not found"));

        ClassEntity createdClass= ClassMapper.toClassEntity(classRequestDto,null);

        assignedTeacher.addSupervisedClass(createdClass);
        assignedGrade.addClass(createdClass);

        ClassEntity saved=classRepository.save(createdClass);

        return ClassMapper.toClassDto(saved);
    }

    @Override
    public ClassResponseDto updateClass(UUID publicId, ClassRequestDto classRequestDto) {

        ClassEntity existingClass= classRepository.findByPublicId(publicId).orElseThrow(()-> new ResourceNotFoundException("Class not found."));

        Teacher assignedTeacher= teacherRepository.findByPublicId(classRequestDto.getTeacherId()).orElseThrow(()-> new ResourceNotFoundException("Teacher not found"));

        Grade assignedGrade = gradeRepository.findByPublicId(classRequestDto.getGradeId()).orElseThrow(()-> new ResourceNotFoundException("Grade not found"));

//        ClassEntity updatedClass= ClassMapper.toClassEntity(classRequestDto,existingClass);

        existingClass= ClassMapper.toClassEntity(classRequestDto,existingClass);


        // Unlink from old teacher/grade if changed
        Teacher oldTeacher = existingClass.getSupervisor();
        if (oldTeacher != null && !oldTeacher.getId().equals(assignedTeacher.getId())) {

            oldTeacher.removeSupervisedClass(existingClass);
            assignedTeacher.addSupervisedClass(existingClass);

        }

        Grade oldGrade = existingClass.getGrade();
        if (oldGrade != null && !oldGrade.getId().equals(assignedGrade.getId())) {
            oldGrade.removeClass(existingClass);
            assignedGrade.addClass(existingClass);
        }


        ClassEntity saved=classRepository.save(existingClass);

        return ClassMapper.toClassDto(saved);

    }

    @Override
    public void deleteClass(UUID publicId) {
        ClassEntity deletedClass=classRepository.findByPublicId(publicId).orElseThrow(() -> new ResourceNotFoundException("Grade not found"));

        // 1. Unlink from Teacher (bidirectional removal)
        Teacher teacher = deletedClass.getSupervisor();
        if (teacher != null) {
            teacher.removeSupervisedClass(deletedClass);
        }

        // 2. Unlink from Grade (bidirectional removal)
        Grade grade = deletedClass.getGrade();
        if (grade != null) {
            grade.removeClass(deletedClass); // uses helper
        }

        classRepository.delete(deletedClass);

    }
}
