package com.example.SMSApp.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    // Email used for communication (will also exist in AppUser)
    @Column(nullable = false,unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    private String img;

    @Column(nullable = false, length = 3)
    private String bloodType;

    @Column(nullable = false)
    private String relation;

    // One-to-many relationship with Student
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    // One-to-one link back to AppUser
    @OneToOne
    @JoinColumn(name = "app_user_id",  referencedColumnName = "id")
    private AppUser appUser;
}
