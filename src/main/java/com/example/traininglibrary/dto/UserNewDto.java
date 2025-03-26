package com.example.traininglibrary.dto;

import java.time.LocalDate;

public record UserNewDto(
        String name,
        LocalDate birthDate,
        String email,
        String phone,
        String address
) {}