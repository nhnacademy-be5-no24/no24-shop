package com.nhnacademy.shop.cart.domain;

import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.member.domain.Member;
import lombok.*;

import javax.persistence.*;

/**
 * 장바구니 Entity
 *
 * @Author: jinjiwon
 * @Date: 05/04/2024
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Builder
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "book_quantity")
    private Long bookQuantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_isbn")
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_no")
    private Member member;
}
