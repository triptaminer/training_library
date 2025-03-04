package com.example.traininglibrary.service;

import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDto> getAuthorById(Long id) {
        return authorRepository.findById(id).map(this::convertToDto);
    }

    public AuthorDto createAuthor(AuthorNewDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.name());
        author.setBirthDate(authorDto.birthDate());
        author.setDeathDate(authorDto.deathDate());
        author.setBio(authorDto.bio());
        Author savedAuthor = authorRepository.save(author);
        return convertToDto(savedAuthor);
    }

    private AuthorDto convertToDto(Author author) {
        return new AuthorDto(author.getId(), author.getVersion(), author.getName(), author.getBirthDate(), author.getDeathDate(), author.getBio());
    }
}
