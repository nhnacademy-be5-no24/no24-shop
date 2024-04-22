package com.nhnacademy.shop.orders.repository.impl;

import com.nhnacademy.shop.book.entity.QBook;
import com.nhnacademy.shop.customer.entity.QCustomer;
import com.nhnacademy.shop.order_detail.domain.QOrderDetail;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.domain.QOrders;
import com.nhnacademy.shop.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.repository.OrdersRepositoryCustom;
import com.nhnacademy.shop.wrap.domain.QWrap;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;


public class OrdersRepositoryImpl extends QuerydslRepositorySupport implements OrdersRepositoryCustom {


    public OrdersRepositoryImpl() {
        super(Orders.class);
    }



    QOrders orders = QOrders.orders;
    QOrderDetail orderDetail = QOrderDetail.orderDetail;
    QCustomer customer = QCustomer.customer;
    QBook book = QBook.book;
    QWrap wrap = QWrap.wrap;
    @Override
    public Page<OrdersListForAdminResponseDto> getOrderList(Pageable pageable) {
        JPQLQuery<Long> cnt = from(orders)
                .select(orders.orderId.count());

        List<OrdersListForAdminResponseDto> content = from(orders)
                .select(Projections.constructor(
                        OrdersListForAdminResponseDto.class,
                        orders.orderId.as("주문번호"),
                        orders.customer.customerName.as("고객이름"),
                        orders.orderDate.as("주문날짜"),
                        orders.orderState.as("주문상태"),
                        orders.address.as("주소"),
                        orders.addressDetail.as("주소상세"),
                        orderDetail.wrap.wrapName.as("포장지이름"),
                        orderDetail.wrap.wrapCost.as("포장지가격"),
                        orderDetail.book.bookTitle.as("도서이름"),
                        orderDetail.book.bookSalePrice.as("도서가격")
                ))
                .innerJoin(orders.customer, customer)
                .innerJoin(orders.orderDetails, orderDetail)
                .innerJoin(orderDetail.wrap, wrap)
                .innerJoin(orderDetail.book, book)
                .orderBy(orders.orderDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, cnt::fetchOne);
    }

    @Override
    public Page<OrdersResponseDto> getOrderListByCustomer(Pageable pageable, Long customerNo) {


        JPQLQuery<OrdersResponseDto> query = from(orders)
                .select(Projections.constructor(
                        OrdersResponseDto.class,
                        orders.orderId.as("주문번호"),
                        orderDetail.book.bookTitle.as("도서"),
                        orderDetail.book.bookSalePrice.as("도서판매가격"),
                        orderDetail.wrap.wrapName.as("포장지이름"),
                        orderDetail.wrap.wrapCost.as("포장가격"),
                        orders.orderDate.as("주문날짜"),
                        orders.receiverName.as("수취인"),
                        orders.receiverPhoneNumber.as("수취인전화번호"),
                        orders.address.as("주소"),
                        orders.addressDetail.as("주소상세"),
                        orders.orderState.as("배송상태")
                ))
                .innerJoin(orders.customer, customer)
                .innerJoin(orders.orderDetails, orderDetail)
                .where(customer.customerNo.eq(customerNo))
                .orderBy(orders.orderDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }
    @Override
    public Optional<OrdersResponseDto> getOrderByOrderId(String orderId) {
        return Optional.of(
                from(orders)
                        .select(Projections.constructor(
                                OrdersResponseDto.class,
                                orders.orderId.as("주문번호"),
                                orderDetail.book.bookTitle.as("도서"),
                                orderDetail.book.bookSalePrice.as("도서판매가격"),
                                orderDetail.wrap.wrapName.as("포장지이름"),
                                orderDetail.wrap.wrapCost.as("포장가격"),
                                orders.orderDate.as("주문날짜"),
                                orders.receiverName.as("수취인"),
                                orders.receiverPhoneNumber.as("수취인전화번호"),
                                orders.address.as("주소"),
                                orders.addressDetail.as("주소상세"),
                                orders.orderState.as("배송상태")

                        ))
                        .innerJoin(orders.orderDetails, orderDetail)
                        .where(orders.orderId.eq(orderId))
                        .fetchOne()
        );
    }
}
