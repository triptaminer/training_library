package com.example.traininglibrary.controller;

import com.example.traininglibrary.dto.BookDto;
import com.example.traininglibrary.dto.BookNewDto;
import com.example.traininglibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/books")
@Validated
@Tag(name = "Books", description = "Operations related to books")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "201", description = "Book created", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Book removed", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Book doesn't exists", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Book doesn't match database record", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json"))
})
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books", description = "Returns all books (paged)")
    @GetMapping
    public ResponseEntity<Page<BookDto>> getAllBooks(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @Operation(summary = "Get all books with books", description = "Returns all books with authors (paged)")
    @GetMapping("/with-books")
    public ResponseEntity<Page<BookDto>> getAllBooksWithBooks(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooksWithBooks(pageable));
    }

    @Operation(summary = "Get book by id", description = "Returns book object by id")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(
            @PathVariable("id")
            @Positive
            @Parameter(description = "Book ID", required = true)
            Long id
    ) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Operation(summary = "Add new book", description = "Adds new book using BookNewDto")
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookNewDto book) {
        BookDto created = bookService.createBook(book);
        URI location = URI.create("/books/" + created.id());

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Edits book", description = "Edits book identified by ID in path with BookDto provided as body")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @Valid @RequestBody BookDto book,
            @PathVariable("id")
            @Positive
            @Parameter(description = "Book ID", required = true)
            Long id
    ) {
        if(!id.equals(book.id())){
            throw new IllegalArgumentException("Path ID and body ID must match.");
        }
        return ResponseEntity.ok(bookService.updateBook(book));
    }

    @Operation(summary = "Removes book", description = "Removes book by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(
            @Valid @RequestBody BookDto book,
            @PathVariable("id")
            @Positive
            @Parameter(description = "Book ID", required = true)
            Long id) {
        if(!id.equals(book.id())){
            throw new IllegalArgumentException("Path ID and body ID must match.");
        }
        bookService.deleteBook(book);
        return ResponseEntity.noContent().build();
    }
}
