package com.nhnacademy.shop.book_author.domain;


import com.nhnacademy.shop.author.domain.Author;
import com.nhnacademy.shop.book.domain.Book;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author : 박동희
 * @Date : 20/03/2024
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookAuthor {
    @EmbeddedId
    private Pk pk;

    @MapsId(value = "bookIsbn")
    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;

    @MapsId(value = "authorId")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "book_isbn")
        private String bookIsbn;
        @Column(name = "author_id")
        private Long authorId;

    }

}
