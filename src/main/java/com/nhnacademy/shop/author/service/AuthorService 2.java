package com.nhnacademy.shop.author.service;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;

import java.util.List;
import java.util.Optional;
/**
 * 저자(Author) service
 *
 * @author : 박동희
 * @date : 2024-03-20
 *
 **/
public interface AuthorService {
    /**
     * 저자 이름으로 동명이인을 포함한 List 조회를 위한 method.
     *
     * @param authorName 조회할 저자 아이디 입니다.
     * @return List<AuthorResponseDto> 저자 정보 리스트가 반환됩니다.
     */
    List<AuthorResponseDto> getAuthorsByAuthorName(String authorName);

    /**
     * 저자 id로 단건 조회를 위한 method.
     *
     * @param authorId 조회할 저자 아이디 입니다.
     * @return Optional<Author> 저자 정보가 반환됩니다.
     */
    Optional<Author> getAuthorById(Long authorId);

    /**
     * 저자를 저장하기 위한 method.
     *
     * @param authorRequestDto 저장하기 위한 저자 정보 입니다.
     * @return AuthorResponseDto 저자 정보가 반환됩니다.
     */
    AuthorResponseDto saveAuthor(AuthorRequestDto authorRequestDto);

    /**
     * 저자를 수정하기 위한 method.
     *
     * @param modifyAuthorRequestDto 수정하기 저자 정보 입니다.
     * @return AuthorResponseDto 저자 정보가 반환됩니다.
     */
    AuthorResponseDto modifyAuthor(ModifyAuthorRequestDto modifyAuthorRequestDto);

    /**
     * 저자 아이디를 통해 저자를 삭제하기 위한 method.
     *
     * @param authorId 저자 아디디 입니다.
     */
    void deleteAuthorById(Long authorId);
}
