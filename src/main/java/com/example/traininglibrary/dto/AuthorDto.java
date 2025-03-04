package com.example.traininglibrary.dto;

import com.example.traininglibrary.entity.Author;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO for sending author", allOf = {Author.class})
public record AuthorDto(Long id, Long version, String name, LocalDate birthDate, LocalDate deathDate, String bio) {}
