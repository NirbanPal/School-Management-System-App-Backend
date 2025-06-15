package com.example.SMSApp.model;


import com.example.SMSApp.model.enums.Day;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Lesson extends BaseEntity{

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

    @Column(nullable = false)
    private String name;

//    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams= new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments= new ArrayList<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances= new ArrayList<>();

    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addExam(Exam exam) {
        this.exams.add(exam);
        exam.setLesson(this);
    }

    public void removeExam(Exam exam) {
        this.exams.remove(exam);
        exam.setLesson(null);
    }

    public void addAssignment(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setLesson(this);
    }

    public void removeAssignment(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setLesson(null);
    }

    public void addAttendance(Attendance attendance) {
        this.attendances.add(attendance);
        attendance.setLesson(this);
    }

    public void removeAttendance(Attendance attendance) {
        this.attendances.remove(attendance);
        attendance.setLesson(null);
    }

}
