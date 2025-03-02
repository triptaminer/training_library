package com.example.traininglibrary.model;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Author extends Person {

    private LocalDate deathDate;

}
