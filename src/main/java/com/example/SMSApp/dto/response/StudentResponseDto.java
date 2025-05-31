package com.example.SMSApp.dto.response;

import com.example.SMSApp.model.*;
import com.example.SMSApp.model.enums.UserSex;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDto {

    private UUID publicId;
    private String name;
    private String surname;
//    private String phone;
    private String address;
    private String bloodType;
    private UserSex sex;
    private LocalDate birthday;
    private Boolean adminApproval;
    private String rollNumber;
    private String idFileName;
    private String idFileType;
    private String idFilePath;
    private String profilePicFileName;
    private String profilePicFileType;
    private String profilePicFilePath;
    private Parent parent;
    private ClassEntity classEntity;
    private Grade grade;
    private Attendance attendances;
    private List<Result> results;
}
