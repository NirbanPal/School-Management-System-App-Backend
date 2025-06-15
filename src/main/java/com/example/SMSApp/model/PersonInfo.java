package com.example.SMSApp.model;

import com.example.SMSApp.model.enums.UserSex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfo {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    // Email used for communication (will also exist in AppUser)
//    @Column(unique = true, nullable = false)
//    private String email;

//    @Column(unique = true, nullable = false, length = 10)
//    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserSex sex;

    @Column(nullable = false, length = 3)
    private String bloodType;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private Boolean adminApproval;

    @Column(nullable = false, unique = true)
    private String idFileName;

    @Column(nullable = false)
    private String idFileType;

    @Column(nullable = false, unique = true)
    private String idFilePath;

    @Column(nullable = false, unique = true)
    private String profilePicFileName;

    @Column(nullable = false)
    private String profilePicFileType;

    @Column(nullable = false, unique = true)
    private String profilePicFilePath;

    //Get full name
    public String getFullName() {
        return name + " " + surname;
    }

}
