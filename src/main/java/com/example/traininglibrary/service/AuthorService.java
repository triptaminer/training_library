package com.example.traininglibrary.service;

import com.example.traininglibrary.entity.Author;
import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.mapper.AuthorMapper;
import com.example.traininglibrary.projection.AuthorIdOnly;
import com.example.traininglibrary.repository.AuthorRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository,
                         AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public Page<AuthorDto> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::convertToDto);
    }

    public Page<AuthorDto> getAllAuthorsWithBooks(Pageable pageable) {

        Page<AuthorIdOnly> page = authorRepository.findAllProjectedBy(pageable);
        List<Long> ids = page.stream().map(AuthorIdOnly::getId).toList();

        List<Author> authorsWithBooks = authorRepository.findAllWithBooksByIdIn(ids);
        Map<Long, Author> authorMap = authorsWithBooks.stream()
                .collect(Collectors.toMap(Author::getId, a -> a));

        List<AuthorDto> dtos = ids.stream()
                .map(authorMap::get)
                .map(authorMapper::convertToDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public AuthorDto getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::convertToDto)
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + id + " not found"));
    }

    public AuthorDto createAuthor(AuthorNewDto authorDto) {

        validateUniqueAuthorAndDates(0L, authorDto.name(), authorDto.birthDate(), authorDto.deathDate());

        Author author = authorMapper.fillAuthorFromDto(new Author(), authorDto);
        return authorMapper.convertToDto(authorRepository.save(author));
    }

    public AuthorDto updateAuthor(AuthorDto editedAuthor){

        Author updatedAuthor = authorRepository.findById(editedAuthor.id())
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + editedAuthor.id() + " not found"));

        if (!updatedAuthor.getVersion().equals(editedAuthor.version())) {
            throw new OptimisticLockException("Version mismatch");
        }
        validateUniqueAuthorAndDates(editedAuthor.id(), editedAuthor.name(), editedAuthor.birthDate(), editedAuthor.deathDate());
        updatedAuthor = authorMapper.fillAuthorFromDto(updatedAuthor, editedAuthor);
        updatedAuthor.setVersion(updatedAuthor.getVersion() + 1);

        return authorMapper.convertToDto(authorRepository.save(updatedAuthor));
    }

    public void deleteAuthor(AuthorDto authorDto) {

        Author author = authorRepository.findById(authorDto.id())
                .orElseThrow(() -> new NoSuchElementException("Author with ID " + authorDto.id() + " not found"));
        if(!author.getVersion().equals(authorDto.version())){
            throw new OptimisticLockException("Version mismatch");
        }
        if(!authorMapper.convertToDto(author).equals(authorDto)){
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

    public List<Author> findAllById(List<Long> ids){
        return authorRepository.findAllById(ids);
    }

}
