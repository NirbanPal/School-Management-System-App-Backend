package com.example.SMSApp.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Unique grade level (e.g., 1, 2, 3, etc.)
    @Column(nullable = false, unique = true)
    private Integer level;

    // One-to-many relationship with students
    @OneToMany(mappedBy = "grade",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    // One-to-many relationship with classes
    @OneToMany(mappedBy = "grade",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassEntity> classes;
}
