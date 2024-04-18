package com.nhnacademy.delivery.order_detail;


import com.nhnacademy.delivery.book.domain.Book;
import com.nhnacademy.delivery.orders.domain.Orders;
import com.nhnacademy.delivery.order_detail.domain.OrderDetail;
import com.nhnacademy.delivery.wrap.domain.Wrap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class OrderDetailEntityTest{

    @Test
    void testOrderDetail() {
        Book book = Book.builder()
                .bookIsbn("1234567890")
                .bookTitle("Test Book")
                .bookDesc("This is a test book")
                .bookPublisher("Test Publisher")
                .bookPublishAt(LocalDate.now())
                .bookFixedPrice(1000L)
                .bookSalePrice(800L)
                .bookIsPacking(false)
                .bookViews(0L)
                .bookStatus(1)
                .bookQuantity(10L)
                .bookImage("book.jpg")
                .build();

        Wrap wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("Test Wrap")
                .wrapCost(200L)
                .build();

        Orders order = Orders.builder()
                .orderId("orderId")
                .orderDate(LocalDate.now())
                .orderState(Orders.OrderState.WAITING)
                .deliveryFee(500L)
                .receiverName("John Doe")
                .receiverPhoneNumber("1234567890")
                .zipcode("12345")
                .address("Test Address")
                .addressDetail("Test Address Detail")
                .req("Test Requirement")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .order(order)
                .book(book)
                .wrap(wrap)
                .build();

        assertEquals(1L, orderDetail.getOrderDetailId());
        assertEquals("orderId", orderDetail.getOrder().getOrderId());
        assertEquals("1234567890", orderDetail.getBook().getBookIsbn());
        assertEquals(1L, orderDetail.getWrap().getWrapId());
    }
}