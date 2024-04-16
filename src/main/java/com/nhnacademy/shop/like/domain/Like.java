package com.nhnacademy.shop.like.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.book.domain.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @Column(name = "member_like_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberLikeId;

    @EmbeddedId
    private Pk pk;

    @MapsId(value = "bookIsbn")
    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;

    @MapsId(value = "customerNo")
    @ManyToOne
    @JoinColumn(name = "customer_no")
    private Member member;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "book_isbn")
        private String bookIsbn;

        @Column(name = "customer_no")
        private Long customerNo;

    }


}
