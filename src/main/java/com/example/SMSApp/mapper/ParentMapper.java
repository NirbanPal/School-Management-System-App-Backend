package com.example.SMSApp.mapper;

import com.example.SMSApp.dto.request.ParentRequestDto;
import com.example.SMSApp.dto.response.ParentResponseDto;
import com.example.SMSApp.model.Parent;
import com.example.SMSApp.model.PersonInfo;

public class ParentMapper {

    public static ParentResponseDto toParentDto(Parent parent) {
        if (parent == null || parent.getPersonInfo() == null) return null;

        PersonInfo info = parent.getPersonInfo();

        return ParentResponseDto.builder()
                .publicId(parent.getPublicId())
                .name(info.getName())
                .surname(info.getSurname())
                .phone(parent.getPhone())
                .address(info.getAddress())
                .bloodType(info.getBloodType())
                .sex(info.getSex())
                .birthday(info.getBirthday())
                .adminApproval(info.getAdminApproval())
                .relation(parent.getRelation())
                .idFileName(info.getIdFileName())
                .idFileType(info.getIdFileType())
                .idFilePath(info.getIdFilePath())
                .profilePicFileName(info.getProfilePicFileName())
                .profilePicFileType(info.getProfilePicFileType())
                .profilePicFilePath(info.getProfilePicFilePath())
                .build();
    }

    public static Parent toParentEntity(ParentRequestDto dto, Parent existingParent) {
        if (dto == null) return null;

        Parent parent = existingParent != null ? existingParent : new Parent();
        PersonInfo personInfo = parent.getPersonInfo() != null ? parent.getPersonInfo() : new PersonInfo();



        personInfo.setName(dto.getName());
        personInfo.setSurname(dto.getSurname());
        personInfo.setAddress(dto.getAddress());
        personInfo.setSex(dto.getSex());
        personInfo.setBloodType(dto.getBloodType());
        personInfo.setBirthday(dto.getBirthday());

//        parent.setPublicId(dto.getPublicId()); // if needed
        parent.setPhone(dto.getPhone());
        parent.setPersonInfo(personInfo);
        parent.setRelation(dto.getRelation());


        return parent;

    }
}
