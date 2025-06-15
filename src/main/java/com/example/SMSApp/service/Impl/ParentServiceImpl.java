package com.example.SMSApp.service.Impl;

import com.example.SMSApp.dto.request.ParentRequestDto;
import com.example.SMSApp.dto.response.ParentResponseDto;
import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.mapper.ParentMapper;
import com.example.SMSApp.mapper.ParentMapper;
import com.example.SMSApp.model.*;
import com.example.SMSApp.model.Parent;
import com.example.SMSApp.model.Parent;
import com.example.SMSApp.model.enums.Role;
import com.example.SMSApp.repository.ParentRepository;
import com.example.SMSApp.repository.ParentRepository;
import com.example.SMSApp.repository.UserRepository;
import com.example.SMSApp.service.ParentService;
import com.example.SMSApp.support.id.CustomIdGeneratorService;
import com.example.SMSApp.support.storage.FileStorageService;
import com.example.SMSApp.utils.FileValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ParentServiceImpl implements ParentService {


    private final ParentRepository parentRepository;

    private final FileStorageService fileStorageService;

    private final UserRepository userRepository;

    @Override
    public List<ParentResponseDto> getAllParents() {
        return parentRepository.findAll()
                .stream()
                .map(ParentMapper::toParentDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParentResponseDto getParentById(UUID publicId) {
        Parent parent = parentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent does not found"));
        return ParentMapper.toParentDto(parent);
    }

    @Override
    public ParentResponseDto createParent(ParentRequestDto parentRequestDto, MultipartFile idFile, MultipartFile profilePic) {
        AppUser appUser = userRepository.findByEmail(parentRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("AppUser not found with Email: " + parentRequestDto.getEmail()));

        // Role check
        if (appUser.getRole() != Role.PARENT) {
            throw new IllegalStateException("AppUser does not have PARENT role.");
        }

        // Prevent duplicate mapping
//        if (appUser.getParent() != null) {
//            throw new IllegalStateException("AppUser is already assigned to a Parent.");
//        }

        //Validating Files
        FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"), 1024 * 1024, "ID Proof");
        FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"), 1024 * 1024, "Profile Pic");


        log.info("Here coming");
        // Store files
        String idFilePath = fileStorageService.storeFile(idFile, "idproof",appUser.getPublicId());
        String profilePicPath = fileStorageService.storeFile(profilePic, "profile",appUser.getPublicId());

        // --- Extract file names and types from MultipartFile ---
        String idFileName = fileStorageService.getCleanFileName(idFile);
        String idFileType = fileStorageService.getFileExtension(idFile);

        String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
        String profilePicFileType = fileStorageService.getFileExtension(profilePic);

        Parent parentObj = ParentMapper.toParentEntity(parentRequestDto,null);

        parentObj.getPersonInfo().setIdFileName(idFileName);
        parentObj.getPersonInfo().setIdFileType(idFileType);
        parentObj.getPersonInfo().setIdFilePath(idFilePath);

        parentObj.getPersonInfo().setProfilePicFileName(profilePicFileName);
        parentObj.getPersonInfo().setProfilePicFileType(profilePicFileType);
        parentObj.getPersonInfo().setProfilePicFilePath(profilePicPath);


        parentObj.setAppUser(appUser);
        Parent saved=parentRepository.save(parentObj);

        return ParentMapper.toParentDto(saved);
    }

    @Override
    public ParentResponseDto updateParent(UUID id, ParentRequestDto parentRequestDto, MultipartFile idFile, MultipartFile profilePic) {
        Parent parent = parentRepository.findByPublicId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent does not found"));

        if(!Objects.equals(parentRequestDto.getEmail(), parent.getAppUser().getEmail()))
            throw new IllegalArgumentException("Email does not match the registered parent email.");

        Parent parentObj = ParentMapper.toParentEntity(parentRequestDto,parent);

        if (idFile != null && !idFile.isEmpty()) {
            FileValidator.validateFile(idFile, List.of("application/pdf", "image/jpeg", "image/png"),1024 * 1024, "ID Proof");
            String idPath = fileStorageService.storeFile(idFile, "idproof",parent.getAppUser().getPublicId());
            String idFileName = fileStorageService.getCleanFileName(idFile);
            String idFileType = fileStorageService.getFileExtension(idFile);
            parentObj.getPersonInfo().setIdFileName(idFileName);
            parentObj.getPersonInfo().setIdFileType(idFileType);
            parentObj.getPersonInfo().setIdFilePath(idPath);
        }

        if (profilePic != null && !profilePic.isEmpty()) {
            FileValidator.validateFile(profilePic, List.of("image/jpeg", "image/png", "image/jpg"),1024 * 1024, "Profile Pic");
            String profilePicPath = fileStorageService.storeFile(profilePic, "profile",parent.getAppUser().getPublicId());
            String profilePicFileName = fileStorageService.getCleanFileName(profilePic);
            String profilePicFileType = fileStorageService.getFileExtension(profilePic);
            parentObj.getPersonInfo().setProfilePicFileName(profilePicFileName);
            parentObj.getPersonInfo().setProfilePicFileType(profilePicFileType);
            parentObj.getPersonInfo().setProfilePicFilePath(profilePicPath);
        }

        Parent updated=parentRepository.save(parentObj);

        return ParentMapper.toParentDto(updated);
    }

    @Override
    @Transactional
    public void deleteParent(UUID publicId) {
        Parent parent = parentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found with id: " + publicId));

        PersonInfo personInfo = parent.getPersonInfo();
        if (personInfo != null) {
            if (personInfo.getProfilePicFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getProfilePicFilePath());
            }
            if (personInfo.getIdFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getIdFilePath());
            }
        }

        AppUser deleteAppUser=parent.getAppUser();

        if (deleteAppUser != null) {
            parent.setAppUser(null);        // unlink to avoid FK constraint issues
        }
        parentRepository.delete(parent);
        if (deleteAppUser != null) {
            userRepository.delete(deleteAppUser); // delete after student is removed
        }

    }

    @Override
    public void approveParentRegistration(UUID publicId) {
        Parent parent =parentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found with id: " + publicId));
        // Only proceed if not already approved
        if (!parent.getPersonInfo().getAdminApproval()) {
            parent.getPersonInfo().setAdminApproval(true);
            parentRepository.save(parent);
        }

    }

    @Override
    public void deleteParentRegistration(UUID publicId) {

        Parent parent = parentRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found with id: " + publicId));

        PersonInfo personInfo = parent.getPersonInfo();
        if (personInfo != null) {
            if (personInfo.getProfilePicFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getProfilePicFilePath());
            }
            if (personInfo.getIdFilePath() != null) {
                fileStorageService.deleteFile(personInfo.getIdFilePath());
            }
        }

        parentRepository.delete(parent);
    }
}
