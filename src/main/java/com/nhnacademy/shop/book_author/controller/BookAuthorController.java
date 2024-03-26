package com.nhnacademy.shop.book_author.controller;

import com.nhnacademy.shop.book_author.dto.BookAuthorRequestDto;
import com.nhnacademy.shop.book_author.dto.BookAuthorResponseDto;
import com.nhnacademy.shop.book_author.service.BookAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/book-author")
@RequiredArgsConstructor
public class BookAuthorController {
    private final BookAuthorService bookAuthorService;

    @GetMapping("/{bookIsbn}")
    public ResponseEntity<List<BookAuthorResponseDto>> getBookAuthorsByBookIsbn(@PathVariable String bookIsbn) {
        List<BookAuthorResponseDto> response = bookAuthorService.getBookAuthorByBookIsbn(bookIsbn);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<List<BookAuthorResponseDto>> getBookAuthorsByAuthorId(@PathVariable Long authorId) {
        List<BookAuthorResponseDto> response = bookAuthorService.getBookAuthorByAuthorId(authorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{authorName}")
    public ResponseEntity<List<BookAuthorResponseDto>> getBookAuthorsByAuthorName(@PathVariable String authorName) {
        List<BookAuthorResponseDto> response = bookAuthorService.getBookAuthorByAuthorName(authorName);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookAuthorResponseDto> saveBookAuthor(@RequestBody BookAuthorRequestDto requestDto) {
        BookAuthorResponseDto responseDto = bookAuthorService.saveBookAuthor(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBookAuthor(@RequestBody BookAuthorRequestDto bookAuthorRequestDto) {
        bookAuthorService.deleteBookAuthor(bookAuthorRequestDto.getBookIsbn(), bookAuthorRequestDto.getAuthorId());
        return ResponseEntity.noContent().build();
    }
}
