package com.nhnacademy.shop.order_detail.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.wrap.domain.Wrap;
import lombok.*;

import javax.persistence.*;

/**
 * 주문 상세(OrderDetail) 테이블.
 *
 * @author : 박동희
 * @date : 2024-04-04
 *
 **/
@Entity
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "wrap_id")
    private Wrap wrap;
}
