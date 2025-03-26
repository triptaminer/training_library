package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Book;
import com.example.traininglibrary.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Set;

@Schema(description = "DTO for sending book with author", allOf = {Book.class})
public record BookNewDto(

        @NotBlank
        @Size(max = 255)
        String title,

        @NotNull
        @Min(1000)
        @Max(2100)
        Integer publishedYear,

        @NotBlank
        Genre genre,

        @Valid
        Set<AuthorMiniDto> authors
        ) {}
