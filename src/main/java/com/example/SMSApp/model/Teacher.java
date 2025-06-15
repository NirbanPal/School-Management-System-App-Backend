package com.example.SMSApp.model;

import com.example.SMSApp.model.enums.UserSex;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Teacher extends BaseEntity{

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

    @Embedded
    private PersonInfo personInfo;

    @Column(unique = true, nullable = false, length = 10)
    private String phone;

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
    private Set<Subject> subjects=new HashSet<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons =new HashSet<>();

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClassEntity> supervisedClasses =new HashSet<>();


    // =============================
    // Bidirectional relationship helpers
    // =============================


    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setTeacher(this);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setTeacher(null);
    }

    public void addSupervisedClass(ClassEntity classEntity) {
        this.supervisedClasses.add(classEntity);
        classEntity.setSupervisor(this);
    }

    public void removeSupervisedClass(ClassEntity classEntity) {
        this.supervisedClasses.remove(classEntity);
        classEntity.setSupervisor(null);
    }

    //For many to many

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.getTeachers().add(this);
    }

    public void removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.getTeachers().remove(this);
    }

    public void clearSubjects() {
        for (Subject subject : new HashSet<>(subjects)) {
            removeSubject(subject);
        }
    }

}