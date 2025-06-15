package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.LessonRequestDto;
import com.example.SMSApp.dto.response.LessonResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.LessonMapper;
import com.example.SMSApp.mapper.ParentMapper;
import com.example.SMSApp.model.ClassEntity;
import com.example.SMSApp.model.Lesson;
import com.example.SMSApp.model.Subject;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.ClassRepository;
import com.example.SMSApp.repository.LessonRepository;
import com.example.SMSApp.repository.SubjectRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final TeacherRepository teacherRepository;

    private final ClassRepository classRepository;

    private final SubjectRepository subjectRepository;


    @Override
    public List<LessonResponseDto> getAllLessons() {
        return lessonRepository.findAll()
                .stream()
                .map(LessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponseDto createLesson(LessonRequestDto lessonRequestDto) {

        Teacher assignedTeacher = teacherRepository.findByPublicId(lessonRequestDto.getTeacherId()).orElseThrow(()-> new ResourceNotFoundException("Teacher Not Found."));

        ClassEntity assignedClass = classRepository.findByPublicId(lessonRequestDto.getClassId()).orElseThrow(()-> new ResourceNotFoundException("Class Not Found."));

        Subject assignedSubject = subjectRepository.findByPublicId(lessonRequestDto.getSubjectId()).orElseThrow(()-> new ResourceNotFoundException("Subject Not Found."));

        Lesson lesson = LessonMapper.toLessonEntity(lessonRequestDto,null);


        //Bidirectional Link
        assignedTeacher.addLesson(lesson);
        assignedClass.addLesson(lesson);
        assignedSubject.addLesson(lesson);

        Lesson savedLesson=lessonRepository.save(lesson);

        return LessonMapper.toLessonDto(savedLesson);
    }

    @Override
    public LessonResponseDto updateLesson(UUID id, LessonRequestDto dto) {

        Lesson existingLesson= lessonRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Lesson Not Found."));

        Teacher assignedTeacher = teacherRepository.findByPublicId(dto.getTeacherId()).orElseThrow(()-> new ResourceNotFoundException("Teacher Not Found."));

        ClassEntity assignedClass = classRepository.findByPublicId(dto.getClassId()).orElseThrow(()-> new ResourceNotFoundException("Class Not Found."));

        Subject assignedSubject = subjectRepository.findByPublicId(dto.getSubjectId()).orElseThrow(()-> new ResourceNotFoundException("Subject Not Found."));

        Lesson lesson= LessonMapper.toLessonEntity(dto,existingLesson);

        Teacher oldTeacher=lesson.getTeacher();
        if(!oldTeacher.getId().equals(assignedTeacher.getId())){
                oldTeacher.removeLesson(lesson);
                assignedTeacher.addLesson(lesson);
        }

        ClassEntity oldClass= lesson.getClassEntity();
        if(!oldClass.getId().equals(assignedClass.getId())){
            oldClass.removeLesson(lesson);
            assignedClass.addLesson(lesson);
        }

        Subject oldSubject = lesson.getSubject();
        if(!oldSubject.getId().equals(assignedSubject.getId())){
            oldSubject.removeLesson(lesson);
            assignedSubject.addLesson(lesson);
        }

        Lesson savedLesson=lessonRepository.save(lesson);

        return LessonMapper.toLessonDto(savedLesson);
    }

    @Override
    public LessonResponseDto getLessonById(UUID id) {

        Lesson getLesson= lessonRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("LessonNot Found."));
        return LessonMapper.toLessonDto(getLesson);
    }

    @Override
    public void deleteLesson(UUID id) {

        Lesson deleteLesson= lessonRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("LessonNot Found."));
        Teacher teacher=deleteLesson.getTeacher();
        if(teacher!=null){
            teacher.removeLesson(deleteLesson);
        }

        ClassEntity classEnt=deleteLesson.getClassEntity();
        if(classEnt!=null){
            classEnt.removeLesson(deleteLesson);
        }

        Subject subject=deleteLesson.getSubject();
        if(subject!=null){
            subject.removeLesson(deleteLesson);
        }

        lessonRepository.delete(deleteLesson);


    }
}
