package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Book;
import com.example.traininglibrary.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "DTO for sending book with author", allOf = {Book.class})
public record BookNewDto(
        //todo validation!!!
        String title,

        Integer publishedYear,

        Genre genre,

        Set<AuthorMiniDto> authors
        ) {}
