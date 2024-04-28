package com.nhnacademy.shop.wrap;

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
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import com.nhnacademy.shop.wrap.repository.WrapInfoRepository;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
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
class WrapInfoRepositoryTest {
    @Autowired
    private WrapInfoRepository wrapInfoRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private WrapRepository wrapRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager entityManager;
    @BeforeEach
    void setup()
    {
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

        Wrap wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("wrapName1")
                .wrapCost(1L)
                .build();
        wrapRepository.save(wrap);

        WrapInfo wrapInfo = WrapInfo.builder()
                .pk(new WrapInfo.Pk(wrap.getWrapId(), orderDetail.getOrderDetailId()))
                .wrap(wrap)
                .orderDetail(orderDetail)
                .amount(1L)
                .build();
        wrapInfoRepository.save(wrapInfo);
    }
    @AfterEach
    public void teardown() {
        this.entityManager.createNativeQuery("ALTER TABLE customer ALTER COLUMN `customer_no` RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE payment ALTER COLUMN `payment_id` RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE wrap ALTER COLUMN `wrap_id` RESTART WITH 1").executeUpdate();
    }
    @Test
    void testFindWrapInfoByOrderDetailId() {
        Optional<Wrap> optionalWrap = wrapRepository.findById(1L);
        WrapInfo.Pk pk = new WrapInfo.Pk(1L, 1L);
        Optional<WrapInfo> optionalWrapInfo = wrapInfoRepository.findById(pk);
        assertTrue(optionalWrapInfo.isPresent());
        assertEquals(1L, optionalWrapInfo.get().getWrap().getWrapId());

    }
}
