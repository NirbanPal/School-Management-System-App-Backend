package com.example.SMSApp.repository;

import com.example.SMSApp.entity.IdSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface IdSequenceRepository extends JpaRepository<IdSequence, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IdSequence> findByEntityName(String entityName);
}

