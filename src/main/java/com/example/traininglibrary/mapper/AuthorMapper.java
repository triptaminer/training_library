package com.example.traininglibrary.mapper;

import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.entity.Author;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    private final BookMiniDtoMapper bookMiniDtoMapper;

    public AuthorMapper(BookMiniDtoMapper bookMiniDtoMapper) {
        this.bookMiniDtoMapper = bookMiniDtoMapper;
    }

    public Author fillAuthorFromDto(Author author, Object dto) {
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

    public AuthorDto convertToDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getVersion(),
                author.getName(),
                author.getBirthDate(),
                author.getDeathDate(),
                author.getBio(),
                author.getBooks().stream()
                        .map(bookMiniDtoMapper::convertToMiniDto)
                        .collect(Collectors.toSet())
        );
    }
}
