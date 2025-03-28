package com.example.traininglibrary.mapper;

import com.example.traininglibrary.dto.BookDto;
import com.example.traininglibrary.dto.BookNewDto;
import com.example.traininglibrary.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final AuthorMiniDtoMapper authorMiniDtoMapper;

    public BookMapper(AuthorMiniDtoMapper authorMiniDtoMapper) {
        this.authorMiniDtoMapper = authorMiniDtoMapper;
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
                        .map(authorMiniDtoMapper::convertToMiniDto)
                        .collect(Collectors.toSet())
        );
    }
}
