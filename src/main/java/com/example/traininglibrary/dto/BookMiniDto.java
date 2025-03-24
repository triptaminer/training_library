package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.entity.Book;
import com.example.traininglibrary.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Schema(description = "DTO for sending book without author", allOf = {Book.class})
public record BookMiniDto(
        Long id,

        String title,

        Integer publishedYear,

        Genre genre
        ) {}
