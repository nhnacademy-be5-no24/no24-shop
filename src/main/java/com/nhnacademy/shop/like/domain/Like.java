package com.nhnacademy.shop.like.domain;

import java.io.Serializable;

import javax.persistence.*;

import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.book.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "like")
public class Like implements Serializable{

    @Id
    @Column(name = "member_like_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberLikeId;

    @ManyToOne
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customer_no", nullable = false)
    private Member member;



}
