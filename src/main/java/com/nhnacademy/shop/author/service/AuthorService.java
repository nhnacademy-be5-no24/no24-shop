package com.nhnacademy.shop.author.service
        ;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorResponseDto> getAuthorsByAuthorName(String authorName);
    Optional<Author> getAuthorById(Long authorId);
    AuthorResponseDto saveAuthor(AuthorRequestDto authorRequestDto);
    AuthorResponseDto modifyAuthor(ModifyAuthorRequestDto modifyAuthorRequestDto);
    void deleteAuthorById(Long authorId);
}
