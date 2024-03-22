package com.nhnacademy.shop.author.service
        ;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;

import java.util.Optional;

public interface AuthorService {
    Optional<Author> getAuthorByAuthorName(String authorName);
    AuthorResponseDto saveAuthor(AuthorRequestDto authorRequestDto);
    AuthorResponseDto modifyAuthor(AuthorRequestDto authorRequestDto);
    void deleteAuthorById(Long authorId);
}
