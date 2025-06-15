package com.example.SMSApp.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grade extends BaseEntity{

    // Unique grade level (e.g., 1, 2, 3, etc.)
    @Column(nullable = false, unique = true)
    private Integer level;

    // One-to-many relationship with students
//    @OneToMany(mappedBy = "grade",cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Student> students= new ArrayList<>();

    // One-to-many relationship with classes
    @OneToMany(mappedBy = "grade",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClassEntity> classes= new HashSet<>();

    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addClass(ClassEntity classEntity) {
        this.classes.add(classEntity);
        classEntity.setGrade(this);
    }

    public void removeClass(ClassEntity classEntity) {
        this.classes.remove(classEntity);
        classEntity.setGrade(null);
    }
}
