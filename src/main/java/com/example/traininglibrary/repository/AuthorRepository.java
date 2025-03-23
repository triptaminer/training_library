package com.example.traininglibrary.repository;

import com.example.traininglibrary.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(attributePaths = {"books"})
    @NonNull
    Optional<Author> findById(Long id);

    Optional<Author> findByNameAndBirthDate(String name, LocalDate birthDate);

    //Page<Author> findAll(Pageable pageable);
    @Query(value = "SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.books",
            countQuery = "SELECT COUNT(a) FROM Author a")
    Page<Author> findAllWithBooks(Pageable pageable);

    @Query("""
    SELECT DISTINCT a FROM Author a
    LEFT JOIN FETCH a.books
    WHERE a.id IN :authorIds
    """)
    List<Author> findAllWithBooksByIdIn(@Param("authorIds") List<Long> authorIds);}