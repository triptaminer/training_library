package com.example.traininglibrary.mapper;

import com.example.traininglibrary.dto.AuthorMiniDto;
import com.example.traininglibrary.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMiniDtoMapper {

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
