package com.example.traininglibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // TPT (Table Per Type)
public abstract class Person extends BaseIncrementalEntity {

    private String name;
    private LocalDate birthDate;

}
