package com.example.traininglibrary.repository;

import com.example.traininglibrary.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndBirthDate(String name, LocalDate birthDate);
}