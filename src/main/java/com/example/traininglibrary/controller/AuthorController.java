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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Validated
@Tag(name = "Authors", description = "Operations related to authors")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Author found", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Author doesn't exists", content = @Content(mediaType = "application/json")),
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
    public AuthorDto createAuthor(@Valid @RequestBody AuthorNewDto author) {
        return authorService.createAuthor(author);
    }
}
