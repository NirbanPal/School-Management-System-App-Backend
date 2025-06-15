package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.TeacherRequestDto;
import com.example.SMSApp.dto.response.ShortDetailsListResponseDto;
import com.example.SMSApp.dto.response.TeacherResponseDto;
import com.example.SMSApp.model.PersonInfo;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;


public class TeacherMapper {

    public static TeacherResponseDto toTeacherDto(Teacher teacher) {
        if (teacher == null || teacher.getPersonInfo() == null) return null;

        PersonInfo info = teacher.getPersonInfo();

        return TeacherResponseDto.builder()
                .publicId(teacher.getPublicId())
                .name(info.getName())
                .surname(info.getSurname())
                .phone(teacher.getPhone())
                .address(info.getAddress())
                .bloodType(info.getBloodType())
                .sex(info.getSex())
                .birthday(info.getBirthday())
                .empId(teacher.getEmpId())
                .adminApproval(info.getAdminApproval())
                .availabilityStatus(teacher.getAvailabilityStatus())
                .qualification(teacher.getQualification())
                .cvFileName(teacher.getCvFileName())
                .cvFileType(teacher.getCvFileType())
                .cvFilePath(teacher.getCvFilePath())
                .experience(teacher.getExperience())
                .idFileName(info.getIdFileName())
                .idFileType(info.getIdFileType())
                .idFilePath(info.getIdFilePath())
                .profilePicFileName(info.getProfilePicFileName())
                .profilePicFileType(info.getProfilePicFileType())
                .profilePicFilePath(info.getProfilePicFilePath())
                .build();
    }

    public static Teacher toTeacherEntity(TeacherRequestDto dto, Teacher existingTeacher) {
        if (dto == null) return null;

        Teacher teacher = existingTeacher != null ? existingTeacher : new Teacher();
        PersonInfo personInfo = teacher.getPersonInfo() != null ? teacher.getPersonInfo() : new PersonInfo();



        personInfo.setName(dto.getName());
        personInfo.setSurname(dto.getSurname());
        personInfo.setAddress(dto.getAddress());
        personInfo.setSex(dto.getSex());
        personInfo.setBloodType(dto.getBloodType());
        personInfo.setBirthday(dto.getBirthday());

//        teacher.setPublicId(dto.getPublicId()); // if needed
        teacher.setPersonInfo(personInfo);
        teacher.setPhone(dto.getPhone());
//        teacher.setEmpId(dto.getEmpId());
        teacher.setQualification(dto.getQualification());
        teacher.setExperience(dto.getExperience());

        return teacher;

    }


    public static ShortDetailsListResponseDto toShortTeacherDetails(Teacher teacher){
        if (teacher == null || teacher.getPersonInfo() == null) return null;

        return ShortDetailsListResponseDto.builder()
                .publicId(teacher.getPublicId())
                .officialId(teacher.getEmpId())
                .name(teacher.getPersonInfo().getFullName())
                .build();
    }
}
