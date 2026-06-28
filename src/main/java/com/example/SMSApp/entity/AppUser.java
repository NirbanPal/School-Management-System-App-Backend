package com.example.SMSApp.entity;


import com.example.SMSApp.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser extends BaseEntity{


    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
//    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime lastLogin;

    // One-to-one association with Student
//    @OneToOne(mappedBy = "appUser")
//    private Student student;

    // One-to-one association with Teacher
//    @OneToOne(mappedBy = "appUser")
//    private Teacher teacher;

    // One-to-one association with Parent
//    @OneToOne(mappedBy = "appUser")
//    private Parent parent;

    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
}

