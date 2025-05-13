package com.example.SMSApp.model;

import com.example.SMSApp.model.enums.UserSex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false, length = 10)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String profileImg;

    @Column(nullable = false, length = 3)
    private String bloodType;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserSex sex;

    @Column(nullable = false)
    private LocalDateTime birthday;

    @Column(length = 10)
    private String empId;

    private Boolean adminApproval;

    @Column(nullable = false)
    private Boolean availabilityStatus;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false)
    private String cv;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false)
    private String idProofImage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id",  referencedColumnName = "id")
    private AppUser appUser;

    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEntity> supervisedClasses;

}
