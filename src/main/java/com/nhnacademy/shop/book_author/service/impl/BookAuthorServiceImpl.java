package com.nhnacademy.shop.book_author.service.impl;

import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.author.exception.NotFoundAuthorException;
import com.nhnacademy.shop.author.repository.AuthorRepository;
import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.book_author.domain.BookAuthor;
import com.nhnacademy.shop.book_author.dto.BookAuthorRequestDto;
import com.nhnacademy.shop.book_author.dto.BookAuthorResponseDto;
import com.nhnacademy.shop.book_author.exception.BookNotFoundException;
import com.nhnacademy.shop.book_author.repository.BookAuthorRepository;
import com.nhnacademy.shop.book_author.service.BookAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookAuthorServiceImpl implements BookAuthorService {

    private BookAuthorRepository bookAuthorRepository;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;


    @Override
    public List<BookAuthorResponseDto> getBookAuthorByBookIsbn(String bookIsbn) {
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByPkBookIsbn(bookIsbn);
        List<BookAuthorResponseDto> dtos = new ArrayList<>();
        for(BookAuthor bookAuthor : bookAuthors){
            BookAuthorResponseDto dto = new BookAuthorResponseDto();
            dto.setBookIsbn(bookAuthor.getPk().getBookIsbn());
            dto.setAuthorId(bookAuthor.getPk().getAuthorId());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<BookAuthorResponseDto> getBookAuthorByAuthorId(Long authorId) {
        List<BookAuthor> bookAuthors = bookAuthorRepository.findByPkAuthorId(authorId);
        List<BookAuthorResponseDto> dtos = new ArrayList<>();
        for(BookAuthor bookAuthor : bookAuthors){
            BookAuthorResponseDto dto = new BookAuthorResponseDto();
            dto.setBookIsbn(bookAuthor.getPk().getBookIsbn());
            dto.setAuthorId(bookAuthor.getAuthor().getAuthorId());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<BookAuthorResponseDto> getBookAuthorByAuthorName(String authorName) {
        List<Author> authors = authorRepository.findAuthorsByAuthorName(authorName);
        List<BookAuthor> bookAuthors = new ArrayList<>();

        for(Author author : authors){
            List<BookAuthor> authorBookAuthors = bookAuthorRepository.findByPkAuthorId(author.getAuthorId());
            bookAuthors.addAll(authorBookAuthors);
        }
        List<BookAuthorResponseDto> dtos = new ArrayList<>();
        for (BookAuthor bookAuthor : bookAuthors) {
            BookAuthorResponseDto dto = new BookAuthorResponseDto();
            dto.setBookIsbn(bookAuthor.getBook().getBookIsbn());
            dto.setAuthorId(bookAuthor.getAuthor().getAuthorId());
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public BookAuthorResponseDto saveBookAuthor(BookAuthorRequestDto bookAuthorRequestDto) {
        Book book = bookRepository.findById(bookAuthorRequestDto.getBookIsbn())
                .orElseThrow(() -> new BookNotFoundException(bookAuthorRequestDto.getBookIsbn()));

        Author author = authorRepository.findById(bookAuthorRequestDto.getAuthorId())
                .orElseThrow(() -> new NotFoundAuthorException());

        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        bookAuthorRepository.save(bookAuthor);

        BookAuthorResponseDto dto = new BookAuthorResponseDto();
        dto.setBookIsbn(bookAuthor.getBook().getBookIsbn());
        dto.setAuthorId(bookAuthor.getAuthor().getAuthorId());
        return dto;
    }

    @Override
    public void deleteBookAuthor(String bookIsbn, Long authorId) {
        bookAuthorRepository.deleteByPkBookIsbnAndPkAuthorId(bookIsbn, authorId);
    }

}
