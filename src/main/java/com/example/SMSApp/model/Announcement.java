package com.example.SMSApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "announcement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Announcement extends BaseEntity{

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "teacher_id", nullable = true)
    private Teacher announcer;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "class_id", nullable = true)
    private ClassEntity classEntity;
}
