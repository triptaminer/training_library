package com.example.traininglibrary.mapper;

import com.example.traininglibrary.dto.BookDto;
import com.example.traininglibrary.dto.BookMiniDto;
import com.example.traininglibrary.dto.BookNewDto;
import com.example.traininglibrary.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMiniDtoMapper {

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
