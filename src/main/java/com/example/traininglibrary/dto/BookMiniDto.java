package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for sending book without author")
public record BookMiniDto(
        Long id,

        String title,

        Integer publishedYear,

        Genre genre
        ) {}
