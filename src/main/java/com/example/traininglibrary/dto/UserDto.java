package com.example.traininglibrary.dto;

import java.time.LocalDate;

public record UserDto(
        Long id,
        String name,
        LocalDate birthDate,
        String email,
        String phone,
        String address
) {}