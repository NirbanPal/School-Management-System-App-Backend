package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.StudentRequestDto;
import com.example.SMSApp.dto.response.StudentResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.StudentMapper;
import com.example.SMSApp.mapper.StudentMapper;
import com.example.SMSApp.mapper.StudentMapper;
import com.example.SMSApp.mapper.StudentMapper;
import com.example.SMSApp.model.*;
import com.example.SMSApp.model.Student;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.ParentRepository;
import com.example.SMSApp.repository.StudentRepository;
import com.example.SMSApp.repository.StudentRepository;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.service.StudentService;
import com.example.SMSApp.support.email.EmailService;
import com.example.SMSApp.support.id.CustomIdGeneratorService;
import com.example.SMSApp.support.storage.FileStorageService;
import com.example.SMSApp.utils.FileValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final FileStorageService fileStorageService;

    private final UserRepository userRepository;

    private final ParentRepository parentRepository;

    private final CustomIdGeneratorService customIdGeneratorService;

    private final PasswordEncoder passwordEncoder;

    private EmailService emailService;


    @Override
    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDto getStudentById(UUID publicId) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Student does not found"));
        return StudentMapper.toStudentDto(student);
    }

    @Override
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto, MultipartFile idFile, MultipartFile profilePic) {
        // Validate and fetch AppUser

        AppUser deleteAppUserParent = userRepository.findByEmail(studentRequestDto.getParentEmail())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with Email: " + studentRequestDto.getParentEmail()));

        // Role check
//        if (deleteAppUserParent.getRole() != Role.PARENT || deleteAppUserParent.getRole() != Role.ADMIN) {
//            throw new IllegalStateException("AppUser must have role PARENT or ADMIN.");
//        }

        if (deleteAppUserParent.getRole() != Role.PARENT) {
            throw new IllegalStateException("AppUser does not have PARENT role.");
        }


        Parent parent = parentRepository.findByAppUser(deleteAppUserParent)
                .orElseThrow(()->new ResourceNotFoundException("Parent details noy found"+studentRequestDto.getParentEmail()));


        //Validating Files
        FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"), 1024 * 1024, "ID Proof");
        FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"), 1024 * 1024, "Profile Pic");


        log.info("Here coming");
        // Store files
        String idFilePath = fileStorageService.storeFile(idFile, "idproof");
        String profilePicPath = fileStorageService.storeFile(profilePic, "profile");

        // --- Extract file names and types from MultipartFile ---
        String idFileName = fileStorageService.getCleanFileName(idFile);
        String idFileType = fileStorageService.getFileExtension(idFile);

        String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
        String profilePicFileType = fileStorageService.getFileExtension(profilePic);

        Student studentObj = StudentMapper.toStudentEntity(studentRequestDto,null);

        studentObj.getPersonInfo().setIdFileName(idFileName);
        studentObj.getPersonInfo().setIdFileType(idFileType);
        studentObj.getPersonInfo().setIdFilePath(idFilePath);

        studentObj.getPersonInfo().setProfilePicFileName(profilePicFileName);
        studentObj.getPersonInfo().setProfilePicFileType(profilePicFileType);
        studentObj.getPersonInfo().setProfilePicFilePath(profilePicPath);

        studentObj.getPersonInfo().setAdminApproval(false);

        studentObj.setParent(parent);
        parent.getStudents().add(studentObj);
        parentRepository.save(parent);
        Student saved=studentRepository.save(studentObj);

        return StudentMapper.toStudentDto(saved);
    }

    @Override
    public StudentResponseDto updateStudent(UUID id, StudentRequestDto studentRequestDto, MultipartFile idFile, MultipartFile profilePic) {
        Student student = studentRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student does not found"));

        AppUser deleteAppUserParent = userRepository.findByEmail(studentRequestDto.getParentEmail())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with Email: " + studentRequestDto.getParentEmail()));

        // Role check
//        if (deleteAppUserParent.getRole() != Role.PARENT || deleteAppUserParent.getRole() != Role.ADMIN) {
//            throw new IllegalStateException("AppUser must have role PARENT or ADMIN.");
//        }

        if (deleteAppUserParent.getRole() != Role.PARENT) {
            throw new IllegalStateException("AppUser does not have PARENT role.");
        }
        Parent parent = parentRepository.findByAppUser(deleteAppUserParent)
                .orElseThrow(()->new ResourceNotFoundException("Parent does not found"));

        if(!Objects.equals(student.getParent(), parent)){
            throw new IllegalArgumentException("Parent and Student do not have any relation.");
        }


        Student studentObj = StudentMapper.toStudentEntity(studentRequestDto,student);

        if (idFile != null && !idFile.isEmpty()) {
            FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"),1024 * 1024, "ID Proof");
            String idPath = fileStorageService.storeFile(idFile, "idproof");
            String idFileName = fileStorageService.getCleanFileName(idFile);
            String idFileType = fileStorageService.getFileExtension(idFile);
            studentObj.getPersonInfo().setIdFileName(idFileName);
            studentObj.getPersonInfo().setIdFileType(idFileType);
            studentObj.getPersonInfo().setIdFilePath(idPath);
        }

        if (profilePic != null && !profilePic.isEmpty()) {
            FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"),1024 * 1024, "Profile Pic");
            String profilePicPath = fileStorageService.storeFile(profilePic, "profile");
            String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
            String profilePicFileType = fileStorageService.getFileExtension(profilePic);
            studentObj.getPersonInfo().setProfilePicFileName(profilePicFileName);
            studentObj.getPersonInfo().setProfilePicFileType(profilePicFileType);
            studentObj.getPersonInfo().setProfilePicFilePath(profilePicPath);
        }

        Student updated=studentRepository.save(studentObj);

        return StudentMapper.toStudentDto(updated);
    }

    @Override
    @Transactional
    public void deleteStudent(UUID publicId) {
        Student student =studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + publicId));
        AppUser deleteAppUser=student.getAppUser();
        if (deleteAppUser != null) {
            student.setAppUser(null);        // unlink to avoid FK constraint issues
        }
        studentRepository.delete(student);
        if (deleteAppUser != null) {
            userRepository.delete(deleteAppUser); // delete after student is removed
        }

    }

    @Override
    public void approveStudentRegistration(UUID publicId) {

        Student student =studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + publicId));

        PersonInfo stuPersonalInfo = student.getPersonInfo();
        // Only proceed if not already approved
        if (!stuPersonalInfo.getAdminApproval()) {
            stuPersonalInfo.setAdminApproval(true);

            // Generate rollNumber only if it does not exist
            if (student.getRollNumber() == null) {
                String rollNumber = customIdGeneratorService.generateStudentId();
                student.setRollNumber(rollNumber);

                if (student.getAppUser()==null){
                    var user = AppUser.builder()
                            .email("NS-"+stuPersonalInfo.getName()+rollNumber)
                            .password(passwordEncoder.encode(stuPersonalInfo.getSurname()+stuPersonalInfo.getBirthday()))
                            .role(Role.STUDENT) // Default role for new registrations
                            .build();
                    student.setAppUser(userRepository.save(user));

                    String message = "Account Details"+stuPersonalInfo.getName()+"\n"
                                    +"Username : "+"NS-"+stuPersonalInfo.getName()+rollNumber+"\n"
                                    +"Password : "+stuPersonalInfo.getSurname()+stuPersonalInfo.getBirthday()
                                    +"Roll Number : "+rollNumber;
                    emailService.sendEmail(student.getParent().getAppUser().getEmail(),"Student Account Info for "+stuPersonalInfo.getName(),message);
                }
            }

            studentRepository.save(student);
        }

    }

    @Override
    @Transactional
    public void deleteStudentRegistration(UUID publicId) {
        Student student = studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + publicId));

        Parent parent = student.getParent();

        if (parent != null) {
            // Maintain bidirectional consistency
            parent.getStudents().remove(student); // remove from parent's list
            student.setParent(null);              // clear back-reference
        }

        // Optional: Hibernate with orphanRemoval=true deletes the student automatically
        // But you can also explicitly delete it if you want clarity
        studentRepository.delete(student);
    }
}
