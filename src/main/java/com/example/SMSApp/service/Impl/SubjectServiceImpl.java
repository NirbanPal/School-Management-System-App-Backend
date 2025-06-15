package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.SubjectRequestDto;
import com.example.SMSApp.dto.response.SubjectResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.StudentMapper;
import com.example.SMSApp.mapper.SubjectMapper;
import com.example.SMSApp.model.Subject;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.SubjectRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final TeacherRepository teacherRepository;

    @Override
    public List<SubjectResponseDto> getAllSubjects() {

        return subjectRepository.findAll()
                .stream()
                .map(SubjectMapper::toSubjectDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponseDto createSubject(SubjectRequestDto subjectRequestDto) {

        Subject subject = SubjectMapper.toSubjectEntity(subjectRequestDto,null);

        return SubjectMapper.toSubjectDto(subjectRepository.save(subject));
    }

    @Override
    public SubjectResponseDto updateSubject(UUID id, SubjectRequestDto subjectRequestDto) {
        Subject existingSubject = subjectRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Subject not found."));
        Subject updatedSubject=SubjectMapper.toSubjectEntity(subjectRequestDto,existingSubject);
        return SubjectMapper.toSubjectDto(updatedSubject);
    }

    @Override
    public SubjectResponseDto getSubjectById(UUID id) {

        Subject getSubject = subjectRepository.findByPublicId(id).orElseThrow(()-> new ResourceNotFoundException("Subject not found."));

        return SubjectMapper.toSubjectDto(getSubject);
    }

    @Override
    public void deleteSubject(UUID id) {
        Subject deletedSubject=subjectRepository.findByPublicId(id).orElseThrow(()->new ResourceNotFoundException("Subject not found."));

        // Detach subject from all associated teachers
        Set<Teacher> teachers = deletedSubject.getTeachers(); // Avoid ConcurrentModificationException because you're modifying the same Set you're iterating over.

        if(teachers!=null && !teachers.isEmpty()){
            for (Teacher teacher : teachers) {
                teacher.removeSubject(deletedSubject); // Uses helper to maintain bidirectional consistency
            }

            // Persist changes to detached teacher-subject relations
            teacherRepository.saveAll(teachers); // Ensure changes are flushed
        }

        // Now delete the subject
        subjectRepository.delete(deletedSubject);

    }
}
