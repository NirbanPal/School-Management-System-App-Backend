package com.example.SMSApp.support.id;

import com.example.SMSApp.model.IdSequence;
import com.example.SMSApp.repository.IdSequenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomIdGeneratorService {

    @Autowired
    private IdSequenceRepository idSequenceRepository;

    @Transactional
    public String generateId(String entityKey, String prefix) {
        IdSequence sequence = idSequenceRepository
                .findByEntityName(entityKey)
                .orElseGet(() -> new IdSequence(entityKey, 0L));

        long nextId = sequence.getLastNumber() + 1;
        sequence.setLastNumber(nextId);

        idSequenceRepository.save(sequence);

        return prefix + String.format("%06d", nextId);
    }

    public String generateTeacherEmpId() {
        return generateId("TEACHER", "EMP-");
    }

    public String generateStudentId() {
        return generateId("STUDENT", "STU-");
    }
}

