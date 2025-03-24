package com.example.traininglibrary.repository;

import com.example.traininglibrary.entity.Book;
import com.example.traininglibrary.projection.BookIdOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"authors"})
    @NonNull
    Optional<Book> findById(@Param("ïd") Long id);

    Page<BookIdOnly> findAllProjectedBy(Pageable pageable);

    Optional<Book> findByTitleAndPublishedYear(String name, int publishedYear);

    //Page<Book> findAll(Pageable pageable);
    @Query(value = "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors",
            countQuery = "SELECT COUNT(b) FROM Book b")
    Page<Book> findAllWithBooks(Pageable pageable);

    @Query("""
    SELECT DISTINCT b FROM Book b
    LEFT JOIN FETCH b.authors
    WHERE b.id IN :bookIds
    """)
    List<Book> findAllWithBooksByIdIn(@Param("bookIds") List<Long> bookIds);

}