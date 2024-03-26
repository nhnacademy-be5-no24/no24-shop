package com.nhnacademy.shop.author.service.impl;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;
import com.nhnacademy.shop.author.exception.NotFoundAuthorException;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import com.nhnacademy.shop.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    public List<AuthorResponseDto> getAuthorsByAuthorName(String authorName) {
         List<Author> authors = authorRepository.findAuthorsByAuthorName(authorName);
         List<AuthorResponseDto> dtos = new ArrayList<>();
         for(Author author : authors){
             AuthorResponseDto dto = new AuthorResponseDto();
             dto.setAuthorId(author.getAuthorId());
             dto.setAuthorName(author.getAuthorName());
             dtos.add(dto);
         }
         return dtos;
    }
    @Override
    public Optional<Author> getAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
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
    public AuthorResponseDto modifyAuthor(ModifyAuthorRequestDto modifyAuthorRequestDto) {
        Optional<Author> optionalAuthor = authorRepository.findById(modifyAuthorRequestDto.getAuthorId());

        if (optionalAuthor.isEmpty()) {
            throw new NotFoundAuthorException();
        }

        Author author = optionalAuthor.get();
        author.setAuthorName(modifyAuthorRequestDto.getAuthorName());
        authorRepository.save(author);

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
