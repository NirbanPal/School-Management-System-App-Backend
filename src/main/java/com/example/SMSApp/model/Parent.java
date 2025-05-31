package com.example.SMSApp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private List<Student> students= new ArrayList<>();

    // One-to-one link back to AppUser
    @OneToOne
    @JoinColumn(name = "app_user_id",  referencedColumnName = "id")
    private AppUser appUser;
}
