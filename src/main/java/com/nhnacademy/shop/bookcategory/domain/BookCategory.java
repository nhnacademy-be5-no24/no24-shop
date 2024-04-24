package com.nhnacademy.shop.bookcategory.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.category.domain.Category;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "book_category")
public class BookCategory {
    @Id
    private Pk pk;

    @MapsId("bookIsbn")
    @ManyToOne
    @JoinColumn(name = "book_isbn")
    @JsonBackReference
    private Book book;

    @MapsId("categoryId")
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter @Setter
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "book_isbn")
        private String bookIsbn;

        @Column(name = "category_id")
        private Long categoryId;
    }
}
