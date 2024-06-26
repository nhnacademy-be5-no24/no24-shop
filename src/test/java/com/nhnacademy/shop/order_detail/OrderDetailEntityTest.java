package com.nhnacademy.shop.order_detail;



import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.orders.domain.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class OrderDetailEntityTest {

    @Test
    void testOrderDetail() {
        Book book = Book.builder()
                .bookIsbn("1234567890")
                .bookTitle("Test Book")
                .bookDesc("This is a test book")
                .bookPublisher("Test Publisher")
                .bookPublishedAt(LocalDate.now())
                .bookFixedPrice(1000L)
                .bookSalePrice(800L)
                .bookIsPacking(false)
                .bookViews(0L)
                .bookStatus(1)
                .bookQuantity(10)
                .bookImage("book.jpg")
                .build();

        Orders order = Orders.builder()
                .orderId("orderId")
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.WAITING)
                .deliveryFee(3000)
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
                .amount(1L)
                .build();

        assertEquals(1L, orderDetail.getOrderDetailId());
        assertEquals("orderId", orderDetail.getOrder().getOrderId());
        assertEquals("1234567890", orderDetail.getBook().getBookIsbn());
        assertEquals(1L, orderDetail.getAmount());
    }
}