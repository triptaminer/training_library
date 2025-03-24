package com.example.traininglibrary.mapper;

import com.example.traininglibrary.dto.AuthorDto;
import com.example.traininglibrary.dto.AuthorMiniDto;
import com.example.traininglibrary.dto.AuthorNewDto;
import com.example.traininglibrary.entity.Author;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthorMapper {

    private final BookMapper bookMapper;

    public AuthorMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
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
        System.out.println(author.getId() + " books: " + author.getBooks().size());
        author.getBooks().forEach(b -> System.out.println("  - book: " + b.getTitle()));
        return new AuthorDto(
                author.getId(),
                author.getVersion(),
                author.getName(),
                author.getBirthDate(),
                author.getDeathDate(),
                author.getBio(),
                author.getBooks().stream()
                        .map(bookMapper::convertToMiniDto)
                        .collect(Collectors.toSet())
        );
    }

    public AuthorMiniDto convertToMiniDto(Author author) {
        return new AuthorMiniDto(
                author.getId(),
                author.getName(),
                author.getBirthDate(),
                author.getDeathDate(),
                author.getBio()
        );
    }

}
