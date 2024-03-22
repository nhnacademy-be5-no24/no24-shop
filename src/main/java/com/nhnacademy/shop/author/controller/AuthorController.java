package com.nhnacademy.shop.author.controller;

import com.nhnacademy.shop.author.dto.AuthorRequestDto;
import com.nhnacademy.shop.author.dto.AuthorResponseDto;
import com.nhnacademy.shop.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class AuthorController {
    private AuthorService authorService;

    @GetMapping("/author/{authorName}")
    public ResponseEntity<AuthorResponseDto> getAuthorByAuthorName(@PathVariable String authorName){
        return authorService.getAuthorByAuthorName(authorName)
                .map(author -> ResponseEntity.ok().body(new AuthorResponseDto(author.getAuthorId(), author.getAuthorName())))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/author")
    public ResponseEntity<AuthorResponseDto> saveAuthor(@RequestBody AuthorRequestDto authorRequestDto){
        AuthorResponseDto authorResponseDto = authorService.saveAuthor(authorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponseDto);
    }

    @PutMapping("/author/{authorName}")
    public ResponseEntity<AuthorResponseDto> modifyAuthor(
            @PathVariable String authorName,
            @RequestBody AuthorRequestDto authorRequestDto
    ){
        authorRequestDto.setAuthorName(authorName);
        AuthorResponseDto modifiedAuthor = authorService.modifyAuthor(authorRequestDto);
        return ResponseEntity.ok().body(modifiedAuthor);
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long authorId) {
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.noContent().build();
    }

}
