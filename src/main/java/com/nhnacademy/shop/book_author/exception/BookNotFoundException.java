package com.nhnacademy.shop.book_author.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String bookIsbn) {
        super("Book not found with ISBN: " + bookIsbn);
    }
}
