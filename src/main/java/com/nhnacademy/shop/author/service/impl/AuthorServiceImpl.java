package com.nhnacademy.shop.author.service.impl;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import com.nhnacademy.shop.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    public Optional<Author> getAuthorByAuthorName(String authorName) {
         return authorRepository.findAuthorByAuthorName(authorName);
    }

    @Override
    public AuthorResponseDto saveAuthor(AuthorRequestDto authorRequestDto) {
        Author author = new Author();
        author.setAuthorName(authorRequestDto.getAuthorName());
        authorRepository.save(author);
        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setAuthorId(author.getAuthorId());
        authorResponseDto.setAuthorName(author.getAuthorName());
        return authorResponseDto;
    }

    @Override
    public AuthorResponseDto modifyAuthor(AuthorRequestDto authorRequestDto) {
        Optional<Author> optionalAuthor = authorRepository.findAuthorByAuthorName(authorRequestDto.getAuthorName());

        if (optionalAuthor.isEmpty()) {
            throw new RuntimeException("잘못된 저자입니다."+optionalAuthor.get().getAuthorName());
        }
        Author author = optionalAuthor.get();
        author.setAuthorName(authorRequestDto.getAuthorName());
        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setAuthorId(author.getAuthorId());
        authorResponseDto.setAuthorName(author.getAuthorName());
        return authorResponseDto;
    }


    @Override
    public void deleteAuthorById(Long authorId) {
        authorRepository.deleteById(authorId);
    }
}
