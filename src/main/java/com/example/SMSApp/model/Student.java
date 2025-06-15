package com.example.SMSApp.model;

import com.example.SMSApp.model.enums.UserSex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Student extends BaseEntity {

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

//    @Column(unique = true, nullable = true)
//    private String email;

    @Embedded
    private PersonInfo personInfo;

    @Column(length = 10,unique = true)
    private String rollNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "grade_id")
//    private Grade grade;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attendance> attendances= new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Result> results= new HashSet<>();

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addAttendance(Attendance attendance) {
        this.attendances.add(attendance);
        attendance.setStudent(this);
    }

    public void removeAttendance(Attendance attendance) {
        this.attendances.remove(attendance);
        attendance.setStudent(null);
    }

    public void addResult(Result result) {
        this.results.add(result);
        result.setStudent(this);
    }

    public void removeResult(Result result) {
        this.results.remove(result);
        result.setStudent(null);
    }

}

