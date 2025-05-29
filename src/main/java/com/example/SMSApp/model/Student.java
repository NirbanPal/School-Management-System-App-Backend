package com.example.SMSApp.model;

import com.example.SMSApp.model.enums.UserSex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(unique = true, nullable = true, length = 10)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String profileImg;

    @Column(nullable = false, length = 3)
    private String bloodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserSex sex;


    @Column(nullable = false)
    private LocalDate birthday;

    @Column(length = 10,unique = true)
    private String rollNumber;

    private Boolean adminApproval;

    @Column(nullable = false)
    private String idProofImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Result> results;

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;


}

