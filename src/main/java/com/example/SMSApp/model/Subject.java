package com.example.SMSApp.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Subject extends BaseEntity{

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "subjects")
    private Set<Teacher> teachers= new HashSet<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons= new HashSet<>();

    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setSubject(this);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setSubject(null);
    }


}
