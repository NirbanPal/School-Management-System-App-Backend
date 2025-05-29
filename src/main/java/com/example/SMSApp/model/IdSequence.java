package com.example.SMSApp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "id_sequence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdSequence {

    @Id
    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "last_number", nullable = false)
    private Long lastNumber;
}