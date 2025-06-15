package com.example.SMSApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ClassEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    // Optional supervisor (teacher)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "teacher_id")
//    private Teacher supervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher supervisor;

    // Grade to which this class belongs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    // Students in this class
    @OneToMany(mappedBy = "classEntity",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students= new HashSet<>();

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons= new HashSet<>();

    // Events for this class
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events= new HashSet<>();

    // Announcements for this class
    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Announcement> announcements= new HashSet<>();


    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addStudent(Student student) {
        this.students.add(student);
        student.setClassEntity(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.setClassEntity(null);
    }


    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setClassEntity(this);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setClassEntity(null);
    }

    public void addEvent(Event event) {
        this.events.add(event);
        event.setClassEntity(this);
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
        event.setClassEntity(null);
    }

    public void addAnnouncement(Announcement announcement) {
        this.announcements.add(announcement);
        announcement.setClassEntity(this);
    }

    public void removeAnnouncement(Announcement announcement) {
        this.announcements.remove(announcement);
        announcement.setClassEntity(null);
    }


}

