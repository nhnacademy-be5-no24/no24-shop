package com.nhnacademy.shop.author.controller;

import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;
import com.nhnacademy.shop.author.exception.NotFoundAuthorException;
import com.nhnacademy.shop.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/**
 * 저자(Author) controller 입니다.
 *
 * @author : 박동희
 * @date : 2024-03-20
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class AuthorController {
    private final AuthorService authorService;

    /**
     * 저자 이름로 동명이인을 포함한 저자 리스트 조회 요청 시 사용되는 메소드입니다.
     *
     * @param authorName 조회를 위한 해당 저자 이름 입니다.
     * @param page       페이지 번호 입니다.
     * @param size       페이지 크기 입니다.
     * @throws ResponseStatusException 저자이름으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/authors/{authorName}")
    public ResponseEntity<Page<AuthorResponseDto>> getAuthorsByAuthorName(
            @PathVariable String authorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try{
            Page<AuthorResponseDto> authorResponseDto = authorService.getAuthorsByAuthorName(authorName, page, size);
            return ResponseEntity.status(HttpStatus.OK).body(authorResponseDto);
        }catch (NotFoundAuthorException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
    }

    /**
     * 저자 셍상 요청 시 사용되는 메소드입니다.
     *
     * @param authorRequestDto 생성을 위한 해당 정보 입니다.
     * @throws ResponseStatusException 존재하지 않을 때 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 201 CREATE 반환합니다.
     */
    @PostMapping("/authors")
    public ResponseEntity<AuthorResponseDto> saveAuthor(@RequestBody AuthorRequestDto authorRequestDto){
        AuthorResponseDto authorResponseDto = authorService.saveAuthor(authorRequestDto);
        if(Objects.isNull(authorResponseDto)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "POST : AUTHOR NOT FOUND");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponseDto);
    }


    /**
     * 저자 수정 요청 시 사용되는 메소드입니다.
     *
     * @param modifyAuthorRequestDto, authorId 수정를 위한 해당 정보 입니다.
     * @throws ResponseStatusException 포장id로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @PutMapping("/authors/{authorId}")
    public ResponseEntity<AuthorResponseDto> modifyAuthor(
            @PathVariable Long authorId,
            @RequestBody ModifyAuthorRequestDto modifyAuthorRequestDto
    ){
        AuthorResponseDto authorResponseDto = authorService.modifyAuthor(modifyAuthorRequestDto);
        if (Objects.isNull(authorResponseDto)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PUT : AUTHOR NOT FOUND");
        }
        return ResponseEntity.ok().body(authorResponseDto);
    }


    /**
     * 저자 아이디로 삭제 요청 시 사용되는 메소드입니다.
     *
     * @param authorId 저자 아이디 입니다.
     * @return 성공했을 때 응답코드 204 NO_CONTENT 반환합니다.
     */
    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.noContent().build();
    }

}
