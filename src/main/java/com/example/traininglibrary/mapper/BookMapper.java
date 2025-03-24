package com.example.traininglibrary.mapper;

import com.example.traininglibrary.dto.BookDto;
import com.example.traininglibrary.dto.BookMiniDto;
import com.example.traininglibrary.dto.BookNewDto;
import com.example.traininglibrary.entity.Book;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BookMapper {

    private final AuthorMapper authorMapper;

    public BookMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public Book fillBookFromDto(Book book, Object dto) {
        if (dto instanceof BookDto bookDto) {
            book.setTitle(bookDto.title());
            book.setPublishedYear(bookDto.publishedYear());
            book.setGenre(bookDto.genre());
        } else if (dto instanceof BookNewDto bookNewDto) {
            book.setTitle(bookNewDto.title());
            book.setPublishedYear(bookNewDto.publishedYear());
            book.setGenre(bookNewDto.genre());
        } else {
            throw new IllegalArgumentException("Unsupported book DTO type");
        }

        return book;
    }

    public BookDto convertToDto(Book book) {
        System.out.println(book.getId() + " books: " + book.getAuthors().size());
        book.getAuthors().forEach(b -> System.out.println("  - author: " + b.getName()));
        return new BookDto(
                book.getId(),
                book.getVersion(),
                book.getTitle(),
                book.getPublishedYear(),
                book.getGenre(),
                book.getAuthors().stream()
                        .map(authorMapper::convertToMiniDto)
                        .collect(Collectors.toSet())
        );
    }

    public BookMiniDto convertToMiniDto(Book book) {
        System.out.println(book.getId() + " books: " + book.getAuthors().size());
        book.getAuthors().forEach(b -> System.out.println("  - author: " + b.getName()));
        return new BookMiniDto(
                book.getId(),
                book.getTitle(),
                book.getPublishedYear(),
                book.getGenre()
        );
    }
}
