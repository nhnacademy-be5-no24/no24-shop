package com.nhnacademy.shop.domain;


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
    private PK pk;

    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    @Embeddable
    public static class PK implements Serializable {
        @Column(name = "book_isbn", insertable = false)
        private String bookIsbn;
        @Column(name = "author_id")
        private Long authorId;

    }

}
