package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.StudentRequestDto;
import com.example.SMSApp.dto.response.ShortDetailsListResponseDto;
import com.example.SMSApp.dto.response.StudentResponseDto;
import com.example.SMSApp.model.Student;
import com.example.SMSApp.model.PersonInfo;
import com.example.SMSApp.model.Student;

public class StudentMapper {

    public static StudentResponseDto toStudentDto(Student student) {
        if (student == null || student.getPersonInfo() == null) return null;

        PersonInfo info = student.getPersonInfo();

        return StudentResponseDto.builder()
                .publicId(student.getPublicId())
                .name(info.getName())
                .surname(info.getSurname())
                .address(info.getAddress())
                .bloodType(info.getBloodType())
                .sex(info.getSex())
                .birthday(info.getBirthday())
                .rollNumber(student.getRollNumber())
                .adminApproval(info.getAdminApproval())
                .classId(student.getClassEntity().getPublicId())
                .className(student.getClassEntity().getName())
                .idFileName(info.getIdFileName())
                .idFileType(info.getIdFileType())
                .idFilePath(info.getIdFilePath())
                .profilePicFileName(info.getProfilePicFileName())
                .profilePicFileType(info.getProfilePicFileType())
                .profilePicFilePath(info.getProfilePicFilePath())
                .build();
    }

    public static Student toStudentEntity(StudentRequestDto dto, Student existingStudent) {
        if (dto == null) return null;

        Student student = existingStudent != null ? existingStudent : new Student();
        PersonInfo personInfo = student.getPersonInfo() != null ? student.getPersonInfo() : new PersonInfo();



        personInfo.setName(dto.getName());
        personInfo.setSurname(dto.getSurname());
        personInfo.setAddress(dto.getAddress());
        personInfo.setSex(dto.getSex());
        personInfo.setBloodType(dto.getBloodType());
        personInfo.setBirthday(dto.getBirthday());

//        student.setPublicId(dto.getPublicId()); // if needed
        student.setPersonInfo(personInfo);


        return student;

    }


    public static ShortDetailsListResponseDto toShortStuDetails(Student stu){
        if (stu == null || stu.getPersonInfo() == null) return null;

        return ShortDetailsListResponseDto.builder()
                .publicId(stu.getPublicId())
                .officialId(stu.getRollNumber())
                .name(stu.getPersonInfo().getFullName())
                .build();
    }

}
