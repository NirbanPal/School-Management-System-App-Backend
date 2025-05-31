package com.example.SMSApp.dto.response;

import com.example.SMSApp.model.Student;
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
public class ParentResponseDto {
    private UUID publicId;
    private String name;
    private String surname;
    private String phone;
    private String address;
    private String bloodType;
    private UserSex sex;
    private LocalDate birthday;
    private Boolean adminApproval;
    private String relation;
    private String idFileName;
    private String idFileType;
    private String idFilePath;
    private String profilePicFileName;
    private String profilePicFileType;
    private String profilePicFilePath;
    private List<Student> students;;
}
