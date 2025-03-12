package com.example.traininglibrary.entity;

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

    @Schema(description = "Death date", example = "2020-09-02")
    private LocalDate deathDate;

    @Lob
    @Schema(description = "Bio of author")
    private String bio;
}
