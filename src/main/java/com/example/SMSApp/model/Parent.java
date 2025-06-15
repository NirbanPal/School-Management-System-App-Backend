package com.example.SMSApp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parent extends BaseEntity {

    @Embedded
    private PersonInfo personInfo;

    @Column(unique = true, nullable = false, length = 10)
    private String phone;

    @Column(nullable = false)
    private String relation;

    // One-to-many relationship with Student
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students= new HashSet<>();

    // One-to-one link back to AppUser
    @OneToOne
    @JoinColumn(name = "app_user_id",  referencedColumnName = "id")
    private AppUser appUser;

    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addStudent(Student student) {
        this.students.add(student);
        student.setParent(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.setParent(null);
    }
}
