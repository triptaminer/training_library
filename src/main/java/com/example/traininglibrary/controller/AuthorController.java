package com.example.traininglibrary.controller;

import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Validated
@Tag(name = "Authors", description = "Operations related to authors")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Author found", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "201", description = "Author created", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "204", description = "Author removed", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Author doesn't exists", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "Author doesn't match database record", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json"))
})
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Get all authors", description = "Returns all authors (paged)")
    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @Operation(summary = "Get author by id", description = "Returns author object by id")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @PathVariable("id")
            @Positive
            @Parameter(description = "Author ID", required = true)
            Long id
    ) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Operation(summary = "Add new author", description = "Adds new author using AuthorNewDto")
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorNewDto author) {
        AuthorDto created = authorService.createAuthor(author);
        URI location = URI.create("/authors/" + created.id());

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Edits author", description = "Edits author identified by  ID in path with AuthorDto provided as body")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @Valid @RequestBody AuthorDto author,
            @PathVariable("id")
            @Positive
            @Parameter(description = "Author ID", required = true)
            Long id
    ) {
        if(!id.equals(author.id())){
            throw new IllegalArgumentException("Path ID and body ID must match.");
        }
        return ResponseEntity.ok(authorService.updateAuthor(author));
    }

    @Operation(summary = "Removes author", description = "Removes author by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthor(
            @Valid @RequestBody AuthorDto author,
            @PathVariable("id")
            @Positive
            @Parameter(description = "Author ID", required = true)
            Long id) {
        if(!id.equals(author.id())){
            throw new IllegalArgumentException("Path ID and body ID must match.");
        }
        authorService.deleteAuthor(author);
        return ResponseEntity.noContent().build();
    }
}
