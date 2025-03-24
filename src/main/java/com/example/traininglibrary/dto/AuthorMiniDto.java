package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "DTO for sending author without books", allOf = {Author.class})
public record AuthorMiniDto(
        Long id,

        String name,

        LocalDate birthDate,

        LocalDate deathDate,

        String bio
) {}