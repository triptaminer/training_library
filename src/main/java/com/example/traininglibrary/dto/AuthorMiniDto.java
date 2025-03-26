package com.example.traininglibrary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO for sending author without books")
public record AuthorMiniDto(
        Long id,

        String name,

        LocalDate birthDate,

        LocalDate deathDate,

        String bio
) {}