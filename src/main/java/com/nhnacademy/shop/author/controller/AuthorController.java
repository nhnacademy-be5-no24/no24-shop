package com.nhnacademy.shop.author.controller;

import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.dto.ModifyAuthorRequestDto;
import com.nhnacademy.shop.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

/**
 * 저자 controller 입니다.
 *
 * @author : 박동희
 * @date : 22/3/2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors/{authorName}")
    public ResponseEntity<List<AuthorResponseDto>> getAuthorsByAuthorName(@PathVariable String authorName){
        List<AuthorResponseDto> authorResponseDtos = authorService.getAuthorsByAuthorName(authorName);
        if(Objects.isNull(authorResponseDtos)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AUTHOR NOT FOUND" + authorName);
        }
        return ResponseEntity.status(HttpStatus.OK).body(authorResponseDtos);
    }
    @PostMapping("/authors")
    public ResponseEntity<AuthorResponseDto> saveAuthor(@RequestBody AuthorRequestDto authorRequestDto){
        AuthorResponseDto authorResponseDto = authorService.saveAuthor(authorRequestDto);
        if(Objects.isNull(authorResponseDto)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AUTHOR NOT FOUND");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponseDto);
    }

    @PutMapping("/authors/{authorId}")
    public ResponseEntity<AuthorResponseDto> modifyAuthor(
            @PathVariable Long authorId,
            @RequestBody ModifyAuthorRequestDto modifyAuthorRequestDto
    ){
        AuthorResponseDto authorResponseDto = authorService.modifyAuthor(modifyAuthorRequestDto);
        if (Objects.isNull(authorResponseDto)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AUTHOR NOT FOUND");
        }
        return ResponseEntity.ok().body(authorResponseDto);
    }

    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.noContent().build();
    }

}
