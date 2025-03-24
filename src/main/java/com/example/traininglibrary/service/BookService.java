package com.example.traininglibrary.service;

import com.example.traininglibrary.dto.AuthorMiniDto;
import com.example.traininglibrary.dto.BookDto;
import com.example.traininglibrary.dto.BookNewDto;
import com.example.traininglibrary.dto.BookMiniDto;
import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.entity.Book;
import com.example.traininglibrary.mapper.BookMapper;
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
    private BookMapper bookMapper;

    public BookService(BookRepository bookRepository,
                       AuthorService authorService, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::convertToDto);
    }

    public Page<BookDto> getAllBooksWithBooks(Pageable pageable) {

        Page<BookIdOnly> page = bookRepository.findAllProjectedBy(pageable);
        List<Long> ids = page.stream().map(BookIdOnly::getId).toList();
        
        List<Book> booksWithBooks = bookRepository.findAllWithBooksByIdIn(ids);
        Map<Long, Book> bookMap = booksWithBooks.stream()
                .collect(Collectors.toMap(Book::getId, a -> a));

        List<BookDto> dtos = ids.stream()
                .map(bookMap::get)
                .map(bookMapper::convertToDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + id + " not found"));
    }

    public BookDto createBook(BookNewDto bookDto) {

        validateUniqueBookAndDates(0L, bookDto.title(), bookDto.publishedYear(), bookDto.authors());

        List<Long> authorIds = bookDto.authors().stream()
                .map(AuthorMiniDto::id)
                .toList();

        Book book = bookMapper.fillBookFromDto(new Book(), bookDto);
        Set<Author> authors = new HashSet<>(authorService.findAllById(authorIds));
        book.setAuthors(authors);

        return bookMapper.convertToDto(bookRepository.save(book));
    }

    public BookDto updateBook(BookDto editedBook){

        Book updatedBook = bookRepository.findById(editedBook.id())
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + editedBook.id() + " not found"));

        if (!updatedBook.getVersion().equals(editedBook.version())) {
            throw new OptimisticLockException("Version mismatch");
        }

        List<Long> authorIds = editedBook.authors().stream()
                .map(AuthorMiniDto::id)
                .toList();

        validateUniqueBookAndDates(editedBook.id(), editedBook.title(), editedBook.publishedYear(), editedBook.authors());
        updatedBook = bookMapper.fillBookFromDto(updatedBook, editedBook);
        Set<Author> authors = new HashSet<>(authorService.findAllById(authorIds));
        updatedBook.setAuthors(authors);
        updatedBook.setVersion(updatedBook.getVersion() + 1);

        return bookMapper.convertToDto(bookRepository.save(updatedBook));
    }

    public void deleteBook(BookDto bookDto) {

        Book book = bookRepository.findById(bookDto.id())
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookDto.id() + " not found"));
        if(!book.getVersion().equals(bookDto.version())){
            throw new OptimisticLockException("Version mismatch");
        }
        if(!bookMapper.convertToDto(book).equals(bookDto)){
            throw new IllegalStateException("Data mismatch: Provided data does not match database record for Book ID " + bookDto.id());
        }
        bookRepository.delete(book);
    }

    private void validateUniqueBookAndDates(long id, String title, Integer publishYear, Set<AuthorMiniDto> authors) {
        Optional<Book> existingBook = bookRepository.findByTitleAndPublishedYear(title, publishYear);

        if (existingBook.isPresent() && !existingBook.get().getId().equals(id)) {
            Set<Long> authorIds = authors.stream()
                    .map(AuthorMiniDto::id)
                    .collect(Collectors.toSet());
            Set<Long> dtoIds = existingBook.get().getAuthors().stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());
            if(authorIds.equals(dtoIds)) {
                throw new IllegalArgumentException("An book with the same author(s), title and published year already exists.");
            }
        }
    }



}
