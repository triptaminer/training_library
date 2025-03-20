package com.example.traininglibrary.service;

import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.repository.AuthorRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.constraints.Positive;
import org.hibernate.bytecode.enhance.VersionMismatchException;
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
        return convertToDto(authorRepository.save(author));
    }

    public AuthorDto updateAuthor(AuthorDto editedAuthor){

        Author updatedAuthor = authorRepository.findById(editedAuthor.id())
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + editedAuthor.id() + " not found"));

        if (!updatedAuthor.getVersion().equals(editedAuthor.version())) {
            throw new OptimisticLockException("Version mismatch");
        }
        validateUniqueAuthorAndDates(editedAuthor.id(), editedAuthor.name(), editedAuthor.birthDate(), editedAuthor.deathDate());
        updatedAuthor = fillAuthorFromDto(updatedAuthor, editedAuthor);
        updatedAuthor.setVersion(updatedAuthor.getVersion() + 1);

        return convertToDto(authorRepository.save(updatedAuthor));
    }

    public void deleteAuthor(AuthorDto authorDto) {

        Author author = authorRepository.findById(authorDto.id())
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + authorDto.id() + " not found"));
        if(!author.getVersion().equals(authorDto.version())){
            throw new OptimisticLockException("Version mismatch");
        }
        if(!convertToDto(author).equals(authorDto)){
            throw new IllegalStateException("Data mismatch: Provided data does not match database record for Author ID " + authorDto.id());
        }
        authorRepository.delete(author);
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
