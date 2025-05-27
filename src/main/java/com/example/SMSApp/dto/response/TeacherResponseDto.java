package com.example.SMSApp.dto;

import com.example.SMSApp.model.enums.UserSex;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {
    private UUID publicId;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private String bloodType;
    private UserSex sex;
    private LocalDate birthday;
    private String empId;
    private Boolean adminApproval;
    private Boolean availabilityStatus;
    private String qualification;
    private String cvFileName;
    private String cvFileType;
    private String cvFilePath;
    private Integer experience;
    private String idFileName;
    private String idFileType;
    private String idFilePath;
    private String profilePicFileName;
    private String profilePicFileType;
    private String profilePicFilePath;
}

