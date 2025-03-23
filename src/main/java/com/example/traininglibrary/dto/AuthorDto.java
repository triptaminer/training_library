package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Schema(description = "DTO for sending author", allOf = {Author.class})
public record AuthorDto(
        Long id,
        Long version,

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @Past(message = "Death date must be in the past")
        LocalDate deathDate,

        String bio,

        List<BookMiniDto> books
) {}