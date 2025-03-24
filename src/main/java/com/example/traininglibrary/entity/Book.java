package com.example.traininglibrary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "published_year"}))
@Data
@EqualsAndHashCode(callSuper = true, exclude = "authors")
public class Book extends BaseIncrementalEntity {

    @Column(nullable = false)
    private String title;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
}