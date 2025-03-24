package com.example.traininglibrary.service;

import com.example.traininglibrary.dto.AuthorMiniDto;
import com.example.traininglibrary.dto.BookDto;
import com.example.traininglibrary.dto.BookNewDto;
import com.example.traininglibrary.dto.BookMiniDto;
import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.entity.Book;
import com.example.traininglibrary.projection.BookIdOnly;
import com.example.traininglibrary.repository.AuthorRepository;
import com.example.traininglibrary.repository.BookRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository,
                       AuthorService authorService, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public Page<BookDto> getAllBooksWithBooks(Pageable pageable) {

        Page<BookIdOnly> page = bookRepository.findAllProjectedBy(pageable);
        List<Long> ids = page.stream().map(BookIdOnly::getId).toList();
        
        List<Book> booksWithBooks = bookRepository.findAllWithBooksByIdIn(ids);
        Map<Long, Book> bookMap = booksWithBooks.stream()
                .collect(Collectors.toMap(Book::getId, a -> a));

        List<BookDto> dtos = ids.stream()
                .map(bookMap::get)
                .map(this::convertToDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));
    }

    public BookDto createBook(BookNewDto bookDto) {

        validateUniqueBookAndDates(0L, bookDto.title(), bookDto.publishedYear());

        Book book = fillBookFromDto(new Book(), bookDto);
        return convertToDto(bookRepository.save(book));
    }

    public BookDto updateBook(BookDto editedBook){

        Book updatedBook = bookRepository.findById(editedBook.id())
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + editedBook.id() + " not found"));

        if (!updatedBook.getVersion().equals(editedBook.version())) {
            throw new OptimisticLockException("Version mismatch");
        }
        validateUniqueBookAndDates(editedBook.id(), editedBook.title(), editedBook.publishedYear());
        updatedBook = fillBookFromDto(updatedBook, editedBook);
        updatedBook.setVersion(updatedBook.getVersion() + 1);

        return convertToDto(bookRepository.save(updatedBook));
    }

    public void deleteBook(BookDto bookDto) {

        Book book = bookRepository.findById(bookDto.id())
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookDto.id() + " not found"));
        if(!book.getVersion().equals(bookDto.version())){
            throw new OptimisticLockException("Version mismatch");
        }
        if(!convertToDto(book).equals(bookDto)){
            throw new IllegalStateException("Data mismatch: Provided data does not match database record for Book ID " + bookDto.id());
        }
        bookRepository.delete(book);
    }

    private void validateUniqueBookAndDates(long id, String title, Integer publishYear) {

        Optional<Book> existingBook = bookRepository.findByTitleAndPublishedYear(title, publishYear);
        if (existingBook.isPresent() && !existingBook.get().getId().equals(id)) {
            throw new IllegalArgumentException("An book with the same name and birth date already exists.");
        }
    }

    private Book fillBookFromDto(Book book, Object dto) {
        List<Long> authorIds;

        if (dto instanceof BookDto bookDto) {
            book.setTitle(bookDto.title());
            book.setPublishedYear(bookDto.publishedYear());
            book.setGenre(bookDto.genre());
            authorIds = bookDto.authors().stream()
                    .map(AuthorMiniDto::id)
                    .toList();
        } else if (dto instanceof BookNewDto bookNewDto) {
            book.setTitle(bookNewDto.title());
            book.setPublishedYear(bookNewDto.publishedYear());
            book.setGenre(bookNewDto.genre());
            authorIds = bookNewDto.authors().stream()
                    .map(AuthorMiniDto::id)
                    .toList();
        } else {
            throw new IllegalArgumentException("Unsupported book DTO type");
        }

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(authorIds));
        book.setAuthors(authors);

        return book;
    }

    private BookDto convertToDto(Book book) {
        System.out.println(book.getId() + " books: " + book.getAuthors().size());
        book.getAuthors().forEach(b -> System.out.println("  - author: " + b.getName()));
        return new BookDto(
                book.getId(),
                book.getVersion(),
                book.getTitle(),
                book.getPublishedYear(),
                book.getGenre(),
                book.getAuthors().stream()
                        .map(a -> authorService.convertToMiniDto(a))
                        .collect(Collectors.toSet())
        );
    }

    private BookMiniDto convertToMiniDto(Book book) {
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
