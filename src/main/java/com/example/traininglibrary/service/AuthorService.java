package com.example.traininglibrary.service;

import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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

    public AuthorDto getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + id + " not found"));
    }

    public AuthorDto createAuthor(AuthorNewDto authorDto) {

        validateUniqueAuthorAndDates(0L, authorDto.name(), authorDto.birthDate(), authorDto.deathDate());

        Author author = fillAuthorFromDto(new Author(), authorDto);
        System.out.println("id "+author.getId());
        return convertToDto(authorRepository.save(author));
    }

    public AuthorDto updateAuthor(AuthorDto editedAuthor){

        Author updatedAuthor = authorRepository.findById(editedAuthor.id())
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + editedAuthor.id() + " not found"));

        validateUniqueAuthorAndDates(editedAuthor.id(), editedAuthor.name(), editedAuthor.birthDate(), editedAuthor.deathDate());
        updatedAuthor = fillAuthorFromDto(updatedAuthor, editedAuthor);

        return convertToDto(authorRepository.save(updatedAuthor));
    }

    private void validateUniqueAuthorAndDates(long id, String name, LocalDate birthDate, LocalDate deathDate) {
        if (deathDate != null && birthDate != null && !deathDate.isAfter(birthDate)) {
            throw new IllegalArgumentException("Death date must be after birth date");
        }

        Optional<Author> existingAuthor = authorRepository.findByNameAndBirthDate(name, birthDate);
        if (existingAuthor.isPresent() && !existingAuthor.get().getId().equals(id)) {
            throw new IllegalArgumentException("An author with the same name and birth date already exists.");
        }
    }

    private Author fillAuthorFromDto(Author author, Object dto) {
        if (dto instanceof AuthorDto authorDto) {
            author.setName(authorDto.name());
            author.setBirthDate(authorDto.birthDate());
            author.setDeathDate(authorDto.deathDate());
            author.setBio(authorDto.bio());
        } else if (dto instanceof AuthorNewDto authorNewDto) {
            author.setName(authorNewDto.name());
            author.setBirthDate(authorNewDto.birthDate());
            author.setDeathDate(authorNewDto.deathDate());
            author.setBio(authorNewDto.bio());
        } else {
            throw new IllegalArgumentException("Unsupported author DTO type");
        }
        return author;
    }
    private AuthorDto convertToDto(Author author) {
        return new AuthorDto(author.getId(), author.getVersion(), author.getName(), author.getBirthDate(), author.getDeathDate(), author.getBio());
    }
}
