package com.example.SMSApp.support.subjectsync;


import com.example.SMSApp.exception.custom.ResourceNotFoundException;
import com.example.SMSApp.model.Subject;
import com.example.SMSApp.model.Teacher;
import com.example.SMSApp.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


// This is the optimised method for many to many subject syncing in teacher entity. This will be used later.
@Component
@RequiredArgsConstructor
public class SubjectSyncHelper {
    private final SubjectRepository subjectRepository;

    /**
     * Efficiently syncs the subject list for a teacher without clearing and re-adding all.
     * Only adds new subjects and removes deleted ones.
     *
     * @param teacher         the teacher entity
     * @param updatedSubjectIds the updated set of subject UUIDs
     */
    public void syncSubjects(Teacher teacher, Set<UUID> updatedSubjectIds) {
        if (updatedSubjectIds == null) return;

        Set<UUID> currentSubjectIds = teacher.getSubjects().stream()
                .map(Subject::getPublicId)
                .collect(Collectors.toSet());

        // Determine subjects to remove
        Set<UUID> toRemove = new HashSet<>(currentSubjectIds);
        toRemove.removeAll(updatedSubjectIds);

        // Determine subjects to add
        Set<UUID> toAdd = new HashSet<>(updatedSubjectIds);
        toAdd.removeAll(currentSubjectIds);

        for (UUID subjectId : toRemove) {
            Subject subject = subjectRepository.findByPublicId(subjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
            teacher.removeSubject(subject);
        }

        for (UUID subjectId : toAdd) {
            Subject subject = subjectRepository.findByPublicId(subjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
            teacher.addSubject(subject);
        }
    }
}
