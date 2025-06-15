package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.StudentRequestDto;
import com.example.SMSApp.dto.response.StudentResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.StudentMapper;
import com.example.SMSApp.model.*;
import com.example.SMSApp.model.Student;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.*;
import com.example.SMSApp.repository.StudentRepository;
import com.example.SMSApp.service.StudentService;
import com.example.SMSApp.support.email.EmailService;
import com.example.SMSApp.support.id.CustomIdGeneratorService;
import com.example.SMSApp.support.storage.FileStorageService;
import com.example.SMSApp.utils.FileValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
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

    private final ClassRepository classRepository;

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

        AppUser appUserParent = userRepository.findByEmail(studentRequestDto.getParentEmail())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with Email: " + studentRequestDto.getParentEmail()));

        // Role check
//        if (appUserParent.getRole() != Role.PARENT || appUserParent.getRole() != Role.ADMIN) {
//            throw new IllegalStateException("AppUser must have role PARENT or ADMIN.");
//        }

        if (appUserParent.getRole() != Role.PARENT) {
            throw new IllegalStateException("AppUser does not have PARENT role.");
        }


        Parent parent = parentRepository.findByAppUser(appUserParent)
                .orElseThrow(()->new ResourceNotFoundException("Parent details not found"+studentRequestDto.getParentEmail()));

        ClassEntity assignedClass= classRepository.findByPublicId(studentRequestDto.getClassId()).orElseThrow(()->new ResourceNotFoundException("Class not found"));

        if(assignedClass.getCapacity()<=assignedClass.getStudents().size()){
            throw new ValidationException("Class is already full. Can not assign more students.");
        }


        //Validating Files
        FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"), 1024 * 1024, "ID Proof");
        FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"), 1024 * 1024, "Profile Pic");


        log.info("Here coming");
        Student student = StudentMapper.toStudentEntity(studentRequestDto,null);
        UUID generatedUUIDForStudentOnly=UUID.randomUUID();
        student.setPublicId(generatedUUIDForStudentOnly);

        // Store files
        String idFilePath = fileStorageService.storeFile(idFile, "idproof",generatedUUIDForStudentOnly);
        String profilePicPath = fileStorageService.storeFile(profilePic, "profile",generatedUUIDForStudentOnly);

        // --- Extract file names and types from MultipartFile ---
        String idFileName = fileStorageService.getCleanFileName(idFile);
        String idFileType = fileStorageService.getFileExtension(idFile);

        String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
        String profilePicFileType = fileStorageService.getFileExtension(profilePic);

//        Student student = StudentMapper.toStudentEntity(studentRequestDto,null);

        student.getPersonInfo().setIdFileName(idFileName);
        student.getPersonInfo().setIdFileType(idFileType);
        student.getPersonInfo().setIdFilePath(idFilePath);

        student.getPersonInfo().setProfilePicFileName(profilePicFileName);
        student.getPersonInfo().setProfilePicFileType(profilePicFileType);
        student.getPersonInfo().setProfilePicFilePath(profilePicPath);

        student.getPersonInfo().setAdminApproval(false);

        // Step 8: Set relationships using helpers
//        student.setClassEntity(assignedClass);
//        student.setParent(parent);

        parent.addStudent(student);               // Bidirectional link
        assignedClass.addStudent(student);
        Student saved=studentRepository.save(student);

        return StudentMapper.toStudentDto(saved);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(UUID id, StudentRequestDto studentRequestDto, MultipartFile idFile, MultipartFile profilePic) {
        Student student = studentRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student does not found"));

        AppUser appUserParent = userRepository.findByEmail(studentRequestDto.getParentEmail())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with Email: " + studentRequestDto.getParentEmail()));

        // Role check
//        if (appUserParent.getRole() != Role.PARENT || appUserParent.getRole() != Role.ADMIN) {
//            throw new IllegalStateException("AppUser must have role PARENT or ADMIN.");
//        }

        if (appUserParent.getRole() != Role.PARENT) {
            throw new IllegalStateException("AppUser does not have PARENT role.");
        }
        Parent parent = parentRepository.findByAppUser(appUserParent)
                .orElseThrow(()->new ResourceNotFoundException("Parent does not found"));

        if(!Objects.equals(student.getParent(), parent)){
            throw new IllegalArgumentException("Parent and Student do not have any relation.");
        }


        student = StudentMapper.toStudentEntity(studentRequestDto,student);

        ClassEntity newAssignedClass = classRepository.findByPublicId(studentRequestDto.getClassId()).orElseThrow(()->new ResourceNotFoundException("Class not found."));

        if (idFile != null && !idFile.isEmpty()) {
            FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"),1024 * 1024, "ID Proof");
            String idPath = fileStorageService.storeFile(idFile, "idproof",student.getPublicId());
            String idFileName = fileStorageService.getCleanFileName(idFile);
            String idFileType = fileStorageService.getFileExtension(idFile);
            student.getPersonInfo().setIdFileName(idFileName);
            student.getPersonInfo().setIdFileType(idFileType);
            student.getPersonInfo().setIdFilePath(idPath);
        }

        if (profilePic != null && !profilePic.isEmpty()) {
            FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"),1024 * 1024, "Profile Pic");
            String profilePicPath = fileStorageService.storeFile(profilePic, "profile",student.getPublicId());
            String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
            String profilePicFileType = fileStorageService.getFileExtension(profilePic);
            student.getPersonInfo().setProfilePicFileName(profilePicFileName);
            student.getPersonInfo().setProfilePicFileType(profilePicFileType);
            student.getPersonInfo().setProfilePicFilePath(profilePicPath);
        }


        //To be continued from here cause now we are working on one to many helpers
        ClassEntity oldClass = student.getClassEntity();
        if (oldClass != null && !oldClass.getId().equals(newAssignedClass.getId())) {
//            oldClass.getStudents().remove(student);
//            newAssignedClass.getStudents().add(student);
//            student.setClassEntity(newAssignedClass);

            oldClass.removeStudent(student);
            newAssignedClass.addStudent(student);
        }


        Student updated=studentRepository.save(student);



        return StudentMapper.toStudentDto(updated);
    }

    @Override
    @Transactional
    public void deleteStudent(UUID publicId) {
        Student student =studentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + publicId));
        // 1. Unlink from Parent (bidirectional removal)
        Parent parent = student.getParent();
        if (parent != null) {
            parent.removeStudent(student); // uses helper to maintain bidirectional consistency
        }

        // 2. Unlink from ClassEntity (bidirectional removal)
        ClassEntity classEntity = student.getClassEntity();
        if (classEntity != null) {
            classEntity.removeStudent(student); // uses helper
        }

        // 3. Delete associated files from storage
        PersonInfo personInfo = student.getPersonInfo();
        if (personInfo != null) {
            if (personInfo.getProfilePicFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getProfilePicFilePath());
            }
            if (personInfo.getIdFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getIdFilePath());
            }
        }

        // 4. Unlink and delete AppUser if exists
        AppUser appUser = student.getAppUser();
        if (appUser != null) {
            student.setAppUser(null); // unlink first to prevent FK violation
            userRepository.delete(appUser);

        }

        // 5. Finally delete student
        studentRepository.delete(student);

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
                            .email("NS-"+rollNumber+"@schoolname.com")
                            .password(passwordEncoder.encode(stuPersonalInfo.getSurname()+stuPersonalInfo.getBirthday()))
                            .role(Role.STUDENT) // Default role for new registrations
                            .build();
                    student.setAppUser(userRepository.save(user));

                    String message = "Account Details"+stuPersonalInfo.getName()+"\n"
                                    +"Username : "+"NS-"+rollNumber+"@schoolname.com"+"\n"
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


        PersonInfo personInfo = student.getPersonInfo();
        if (personInfo != null) {
            if (personInfo.getProfilePicFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getProfilePicFilePath());
            }
            if (personInfo.getIdFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getIdFilePath());
            }
        }


        Parent parent = student.getParent();
        if (parent != null) {
            parent.removeStudent(student); // uses helper to maintain bidirectional consistency
        }

        // 2. Unlink from ClassEntity (bidirectional removal)
        ClassEntity classEntity = student.getClassEntity();
        if (classEntity != null) {
            classEntity.removeStudent(student); // uses helper
        }

        // Optional: Hibernate with orphanRemoval=true deletes the student automatically
        // But you can also explicitly delete it if you want clarity
        studentRepository.delete(student);
    }
}
