package com.example.SMSApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Exam extends BaseEntity{

    @EqualsAndHashCode.Include
    private UUID publicId;  // inherited from BaseEntity, but explicitly included

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taken_by_teacher_id", nullable = false)
    private Teacher takenBy;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true   )
    private Set<Result> results= new HashSet<>();

    // =============================
    // Bidirectional relationship helpers
    // =============================

    public void addResult(Result result) {
        this.results.add(result);
        result.setExam(this);
    }

    public void removeResult(Result result) {
        this.results.remove(result);
        result.setExam(null);
    }
}
