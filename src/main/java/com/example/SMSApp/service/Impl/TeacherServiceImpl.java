package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.TeacherRequestDto;
import com.example.SMSApp.dto.response.ShortDetailsListResponseDto;
import com.example.SMSApp.dto.response.TeacherResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.TeacherMapper;
import com.example.SMSApp.model.AppUser;
import com.example.SMSApp.model.PersonInfo;
import com.example.SMSApp.model.Subject;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.SubjectRepository;
import com.example.SMSApp.repository.TeacherRepository;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.support.storage.FileStorageService;
import com.example.SMSApp.service.TeacherService;
import com.example.SMSApp.support.id.CustomIdGeneratorService;
import com.example.SMSApp.utils.FileValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private final FileStorageService fileStorageService;

    private final UserRepository userRepository;

    private final CustomIdGeneratorService customIdGeneratorService;

    private final SubjectRepository subjectRepository;

    @Override
    public List<ShortDetailsListResponseDto> getAllTeacherList() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::toShortTeacherDetails)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherResponseDto> getAllTeachers() {

        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::toTeacherDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherResponseDto getTeacherById(UUID publicId){
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher does not found"));
        return TeacherMapper.toTeacherDto(teacher);
    }

    @Override
    public TeacherResponseDto createTeacher(TeacherRequestDto teacherRequestDto, MultipartFile cv, MultipartFile idFile, MultipartFile profilePic) {
        // Validate and fetch AppUser

        AppUser appUser = userRepository.findByEmail(teacherRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with Email: " + teacherRequestDto.getEmail()));

        // Role check
        if (appUser.getRole() != Role.TEACHER) {
            throw new IllegalStateException("AppUser does not have TEACHER role. Teacher is not authenticated");
        }

        // Prevent duplicate mapping
//        if (appUser.getTeacher() != null) {
//            throw new IllegalStateException("AppUser is already assigned to a Teacher.");
//        }

        //Validating Files
        FileValidator.validateFile(cv, List.of("application/pdf"), 1024 * 1024, "CV");
        FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"), 1024 * 1024, "ID Proof");
        FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"), 1024 * 1024, "Profile Pic");


        log.info("Here coming");
        // Store files
        String cvPath = fileStorageService.storeFile(cv, "cv",appUser.getPublicId());
        String idFilePath = fileStorageService.storeFile(idFile, "idproof",appUser.getPublicId());
        String profilePicPath = fileStorageService.storeFile(profilePic, "profile",appUser.getPublicId());

        // --- Extract file names and types from MultipartFile ---
        String idFileName = fileStorageService.getCleanFileName(idFile);
        String idFileType = fileStorageService.getFileExtension(idFile);

        String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
        String profilePicFileType = fileStorageService.getFileExtension(profilePic);

        String cvFileName = fileStorageService.getCleanFileName(cv);
        String cvFileType = fileStorageService.getFileExtension(cv);


        Teacher teacherObj = TeacherMapper.toTeacherEntity(teacherRequestDto,null);
        teacherObj.setAvailabilityStatus(false);
        teacherObj.setCvFileName(cvFileName);
        teacherObj.setCvFileType(cvFileType);
        teacherObj.setCvFilePath(cvPath);

        teacherObj.getPersonInfo().setIdFileName(idFileName);
        teacherObj.getPersonInfo().setIdFileType(idFileType);
        teacherObj.getPersonInfo().setIdFilePath(idFilePath);

        teacherObj.getPersonInfo().setProfilePicFileName(profilePicFileName);
        teacherObj.getPersonInfo().setProfilePicFileType(profilePicFileType);
        teacherObj.getPersonInfo().setProfilePicFilePath(profilePicPath);

        teacherObj.getPersonInfo().setAdminApproval(false);

        teacherObj.setAppUser(appUser);
        Teacher saved=teacherRepository.save(teacherObj);

        return TeacherMapper.toTeacherDto(saved);

    }

    @Override
    public TeacherResponseDto updateTeacher(UUID id, TeacherRequestDto teacherRequestDto, MultipartFile cv, MultipartFile idFile, MultipartFile profilePic) {
        Teacher teacher = teacherRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher does not found"));

        if(!Objects.equals(teacherRequestDto.getEmail(), teacher.getAppUser().getEmail()))
                    throw new IllegalArgumentException("Email does not match the registered teacher email.");

        Teacher teacherObj = TeacherMapper.toTeacherEntity(teacherRequestDto,teacher);

        if (cv != null && !cv.isEmpty()) {
            FileValidator.validateFile(cv, List.of("application/pdf"),1024 * 1024, "CV");
            String cvPath = fileStorageService.storeFile(cv, "cv",teacher.getAppUser().getPublicId());
            String cvFileName = fileStorageService.getCleanFileName(cv);
            String cvFileType = fileStorageService.getFileExtension(cv);
            teacherObj.setCvFileName(cvFileName);
            teacherObj.setCvFileType(cvFileType);
            teacherObj.setCvFilePath(cvPath);
        }

        if (idFile != null && !idFile.isEmpty()) {
            FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"),1024 * 1024, "ID Proof");
            String idPath = fileStorageService.storeFile(idFile, "idproof",teacher.getAppUser().getPublicId());
            String idFileName = fileStorageService.getCleanFileName(idFile);
            String idFileType = fileStorageService.getFileExtension(idFile);
            teacherObj.getPersonInfo().setIdFileName(idFileName);
            teacherObj.getPersonInfo().setIdFileType(idFileType);
            teacherObj.getPersonInfo().setIdFilePath(idPath);
        }

        if (profilePic != null && !profilePic.isEmpty()) {
            FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"),1024 * 1024, "Profile Pic");
            String profilePicPath = fileStorageService.storeFile(profilePic, "profile",teacher.getAppUser().getPublicId());
            String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
            String profilePicFileType = fileStorageService.getFileExtension(profilePic);
            teacherObj.getPersonInfo().setProfilePicFileName(profilePicFileName);
            teacherObj.getPersonInfo().setProfilePicFileType(profilePicFileType);
            teacherObj.getPersonInfo().setProfilePicFilePath(profilePicPath);
        }


        // Handle subject updates only if not null
        if (teacherRequestDto.getSubjectIds() != null) {
            teacherObj.clearSubjects();
            for (UUID subjectId : teacherRequestDto.getSubjectIds()) {
                Subject subject = subjectRepository.findByPublicId(subjectId)
                        .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
                teacherObj.addSubject(subject);
            }
        }

        Teacher updated=teacherRepository.save(teacherObj);

        return TeacherMapper.toTeacherDto(updated);
    }

    @Override
    @Transactional
    public void deleteTeacher(UUID publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + publicId));

        //Deleting Files
        PersonInfo personInfo = teacher.getPersonInfo();
        if (personInfo != null) {
            if (personInfo.getProfilePicFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getProfilePicFilePath());
            }
            if (personInfo.getIdFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getIdFilePath());
            }
        }
        if (teacher.getCvFilePath() != null) {
            fileStorageService.deleteFile(teacher.getCvFilePath());
        }

        AppUser deleteAppUser=teacher.getAppUser();
        if(deleteAppUser!=null){
            teacher.setAppUser(null);
        }
        teacherRepository.delete(teacher);
        if (deleteAppUser != null) {
            userRepository.delete(deleteAppUser);
        }
    }

    @Override
    @Transactional
    public void approveTeacherRegistration(UUID publicId) {

        Teacher teacher =teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + publicId));
        // Only proceed if not already approved
        if (!teacher.getPersonInfo().getAdminApproval()) {
            teacher.getPersonInfo().setAdminApproval(true);

            // Generate empId only if it does not exist
            if (teacher.getEmpId() == null) {
                String empId = customIdGeneratorService.generateTeacherEmpId();
                teacher.setEmpId(empId);
            }

            teacherRepository.save(teacher);
        }
    }

    @Override
    public void deleteTeacherRegistration(UUID publicId) {
        Teacher teacher = teacherRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + publicId));

        //Deleting Files
        PersonInfo personInfo = teacher.getPersonInfo();
        if (personInfo != null) {
            if (personInfo.getProfilePicFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getProfilePicFilePath());
            }
            if (personInfo.getIdFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getIdFilePath());
            }
        }
        if (teacher.getCvFilePath() != null) {
            fileStorageService.deleteFile(teacher.getCvFilePath());
        }


        teacherRepository.delete(teacher);
    }
}
