package com.example.SMSApp.model;

import com.example.SMSApp.model.enums.UserSex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseEntity{

    @Embedded
    private PersonInfo personInfo;

    @Column(length = 10,unique = true)
    private String empId;

    @Column(nullable = false)
    private Boolean availabilityStatus;

    @Column(nullable = false)
    private String qualification;

    @Column(nullable = false, unique = true)
    private String cvFileName;

    @Column(nullable = false)
    private String cvFileType;

    @Column(nullable = false, unique = true)
    private String cvFilePath;

    @Column(nullable = false)
    private Integer experience;

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
