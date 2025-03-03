package com.example.traininglibrary.repository;

import com.example.traininglibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}