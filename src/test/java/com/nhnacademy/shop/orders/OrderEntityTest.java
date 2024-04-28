package com.nhnacademy.shop.orders;

import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.payment.domain.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@AutoConfigureMockMvc
class OrderEntityTest {

    @Test
    void testOrder(){
        Orders order = Orders.builder()
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.WAITING)
                .deliveryFee(1000)
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


        order.modifyState(Orders.OrderState.SHIPPING);

        Assertions.assertEquals(Orders.OrderState.SHIPPING, order.getOrderState());
    }
}
