package com.nhnacademy.shop.order_detail;

import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.repository.OrdersRepository;
import com.nhnacademy.shop.payment.domain.Payment;
import com.nhnacademy.shop.payment.repository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup(){
        Payment payment = Payment.builder()
                .paymentId(1L).paymentName("name").build();
        paymentRepository.save(payment);

        Customer customer = Customer.builder()
                .customerNo(1L)
                .customerId("id")
                .customerPassword("password")
                .customerName("name")
                .customerPhoneNumber("number")
                .customerEmail("email")
                .customerBirthday(LocalDate.of(2024, 4, 14))
                .customerRole("role")
                .build();
        customerRepository.save(customer);

        Book book = Book.builder()
                .bookIsbn("ABC123")
                .bookTitle("The Little Prince")
                .bookDesc("hi")
                .bookPublisher("생택쥐페리")
                .bookPublishedAt(LocalDate.now())
                .bookFixedPrice(10000L)
                .bookSalePrice(10000L)
                .bookIsPacking(true)
                .bookViews(203L)
                .bookStatus(1)
                .bookQuantity(15)
                .bookImage("img.png")
                .build();
        bookRepository.save(book);

        Orders order = Orders.builder()
                .orderId("orderId1")
                .orderDate(LocalDateTime.now())
                .shippingDate(LocalDateTime.now())
                .orderState(Orders.OrderState.WAITING)
                .deliveryFee(1L)
                .payment(payment)
                .customer(customer)
                .receiverName("receiverName1")
                .receiverPhoneNumber("1")
                .zipcode("1")
                .address("address1")
                .addressDetail("addressdetail1")
                .req("req1")
                .build();
        ordersRepository.save(order);

        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .order(order)
                .book(book)
                .amount(1L)
                .build();
        orderDetailRepository.save(orderDetail);
    }
    @AfterEach
    public void teardown() {
        this.entityManager.createNativeQuery("ALTER TABLE order_detail ALTER COLUMN `order_detail_id` RESTART WITH 1").executeUpdate();
    }
    @Test
    void testRepository(){
        Optional<OrderDetail> optionalOrderDetail = orderDetailRepository.findById(1L);
        assertTrue(optionalOrderDetail.isPresent());
        assertEquals(1L, optionalOrderDetail.get().getOrderDetailId());
    }
}
