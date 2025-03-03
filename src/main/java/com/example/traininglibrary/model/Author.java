package com.example.traininglibrary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Author of books")
public class Author extends Person {

    @Schema(description = "Dátum úmrtia", example = "1973-09-02")
    private LocalDate deathDate;

    @Lob
    @Schema(description = "Boi of author")
    private String bio;
}
