package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Author;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "DTO for adding author", allOf = {Author.class})
public record AuthorNewDto(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @Past(message = "Death date must be in the past")
        LocalDate deathDate,

        String bio,

        //removes fields from swagger
        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        Long version
) {}
