package com.example.traininglibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // TPT (Table Per Type)
@EqualsAndHashCode(callSuper = true)
public abstract class Person extends BaseIncrementalEntity {

    private String name;

    private LocalDate birthDate;

}
