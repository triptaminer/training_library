package com.example.traininglibrary.entity;

import com.example.traininglibrary.dto.BookMiniDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = "books")
@Schema(description = "Author of books")
public class Author extends Person {

    @Schema(description = "Death date", example = "2020-09-02")
    private LocalDate deathDate;

    @Lob
    @Schema(description = "Bio of author")
    private String bio;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    @Schema(description = "List of books")
    private Set<Book> books = new HashSet<>();
}
