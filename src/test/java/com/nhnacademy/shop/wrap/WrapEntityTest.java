package com.nhnacademy.shop.wrap;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.payment.domain.Payment;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class WrapEntityTest {

    @Test
    void testWrapEntityGetterTest(){

        Wrap wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("wrapName")
                .wrapCost(1L)
                .build();
        Long wrapId = wrap.getWrapId();
        String wrapName = wrap.getWrapName();
        Long wrapCost = wrap.getWrapCost();

        assertEquals(Long.valueOf(1L), wrapId);
        assertEquals("wrapName", wrapName);
        assertEquals(Long.valueOf(1L), wrapCost);
    }

    @Test
    void testWrapInfoEntityTest(){
        Wrap wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("wrapName")
                .wrapCost(1L)
                .build();

        Orders order = Orders.builder()
                .orderDate(LocalDateTime.now())
                .shippingDate(LocalDateTime.now())
                .orderState(Orders.OrderState.WAITING)
                .deliveryFee(1000L)
                .payment(new Payment())
                .customer(new Customer())
                .receiverName("John Doe")
                .receiverPhoneNumber("123-456-7890")
                .zipcode("12345")
                .address("123 Main St")
                .addressDetail("Apt 101")
                .req("Please deliver before 5 PM")
                .orderDetails(Collections.emptyList())
                .build();

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

        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .order(order)
                .book(book)
                .amount(1L)
                .build();

        WrapInfo wrapInfo = WrapInfo.builder()
                .orderDetail(orderDetail)
                .wrap(wrap)
                .amount(1L)
                .build();

        Long wrapId = wrapInfo.getWrap().getWrapId();
        String wrapName = wrapInfo.getWrap().getWrapName();
        Long wrapCost = wrapInfo.getWrap().getWrapCost();

        assertEquals(Long.valueOf(1L), wrapId);
        assertEquals("wrapName", wrapName);
        assertEquals(Long.valueOf(1L), wrapCost);


    }

}
