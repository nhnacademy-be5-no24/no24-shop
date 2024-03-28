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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;


    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> getAuthorsByAuthorName(String authorName) {
         List<Author> authors = authorRepository.findAuthorsByAuthorName(authorName);
         List<AuthorResponseDto> dtos = new ArrayList<>();
         for(Author author : authors){
             AuthorResponseDto dto = new AuthorResponseDto(author.getAuthorId(), author.getAuthorName());
             dtos.add(dto);
         }
         return dtos;
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Author> getAuthorById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    @Transactional
    public AuthorResponseDto saveAuthor(AuthorRequestDto authorRequestDto) {
        Author author = new Author(null, authorRequestDto.getAuthorName());
        Author createdAuthor = authorRepository.save(author);
        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setAuthorId(createdAuthor.getAuthorId());
        authorResponseDto.setAuthorName(createdAuthor.getAuthorName());
        return authorResponseDto;
    }

    @Override
    @Transactional
    public AuthorResponseDto modifyAuthor(ModifyAuthorRequestDto modifyAuthorRequestDto) {
        Optional<Author> optionalAuthor = authorRepository.findById(modifyAuthorRequestDto.getAuthorId());

        if (optionalAuthor.isEmpty()) {
            throw new NotFoundAuthorException();
        }

        Author author = new Author(optionalAuthor.get().getAuthorId(), optionalAuthor.get().getAuthorName());
        Author updatedAuthor = authorRepository.save(author);

        AuthorResponseDto authorResponseDto = new AuthorResponseDto();
        authorResponseDto.setAuthorId(updatedAuthor.getAuthorId());
        authorResponseDto.setAuthorName(updatedAuthor.getAuthorName());
        return authorResponseDto;
    }


    @Override
    @Transactional
    public void deleteAuthorById(Long authorId) {
        if(authorRepository.findById(authorId).isEmpty()){
            throw new NotFoundAuthorException();
        }
        authorRepository.deleteById(authorId);
    }
}
