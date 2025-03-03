package com.example.traininglibrary.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Author extends Person {

    private LocalDate deathDate;

    @Lob
    private String bio;
}
