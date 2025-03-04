package com.example.traininglibrary.controller;

import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.service.AuthorService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @PathVariable(name = "id")
            @Parameter(description = "Author ID", in = ParameterIn.PATH, required = true)
            @Positive(message = "ID must be a positive number")
            Long id
    ) {
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AuthorDto createAuthor(@Valid @RequestBody AuthorNewDto author) {
        return authorService.createAuthor(author);
    }
}
