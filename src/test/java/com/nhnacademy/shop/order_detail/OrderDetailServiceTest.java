package com.nhnacademy.shop.order_detail;

import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.order_detail.dto.OrderDetailResponseDto;
import com.nhnacademy.shop.order_detail.repository.OrderDetailRepository;
import com.nhnacademy.shop.order_detail.service.OrderDetailService;
import com.nhnacademy.shop.order_detail.service.impl.OrderDetailServiceImpl;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.exception.NotFoundOrderException;
import com.nhnacademy.shop.orders.repository.OrdersRepository;
import com.nhnacademy.shop.orders.service.OrdersService;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
@WebAppConfiguration
class OrderDetailServiceTest {

    @Mock
    private final OrderDetailRepository orderDetailRepository = mock(OrderDetailRepository.class);
    @Mock
    private final OrdersRepository ordersRepository = mock(OrdersRepository.class);
    private OrderDetailService orderDetailService;
    private OrdersService ordersService;
    @BeforeEach
    void setup(){
        orderDetailService = new OrderDetailServiceImpl(orderDetailRepository, ordersRepository);

    }

    @Test
    void getOrderDetailByOrderIdTest(){
        Book book = Book.builder()
                .bookIsbn("isbn")
                .bookTitle("title")
                .bookDesc("desc")
                .bookPublisher("publisher")
                .bookPublishedAt(LocalDate.now())
                .bookFixedPrice(1L)
                .bookSalePrice(1L)
                .bookIsPacking(false)
                .bookViews(1L)
                .bookStatus(1)
                .bookQuantity(1)
                .bookImage("image")
                .build();

        Orders order = Orders.builder()
                .orderId("orderId")
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.WAITING)
                .deliveryFee(3)
                .receiverName("name")
                .receiverPhoneNumber("number")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("addressDetail")
                .req("req")
                .build();

        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .order(order)
                .book(book)
                .amount(1L)
                .build();
        Wrap wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("name")
                .wrapCost(1L)
                .build();
        WrapInfo wrapInfo = WrapInfo.builder()
                .orderDetail(orderDetail)
                .wrap(wrap)
                .amount(1L)
                .build();

        TagResponseDto tagResponseDto = TagResponseDto.builder()
                .tagId(1L)
                .tagName("name")
                .build();

        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .bookIsbn("isbn")
                .bookTitle("title")
                .bookDescription("description")
                .bookPublisher("publisher")
                .publishedAt(LocalDate.now())
                .bookFixedPrice(1L)
                .bookSalePrice(1L)
                .bookIsPacking(false)
                .bookViews(1L)
                .bookStatus(1)
                .bookQuantity(1)
                .bookImage("image")
                .tags(List.of(tagResponseDto))
                .author("author")
                .likes(1L)
                .build();

        OrderDetailResponseDto.WrapDto wrapDto = OrderDetailResponseDto.WrapDto.builder()
                .wrapName("name")
                .wrapCost(1L)
                .quantity(1L)
                .build();

        OrderDetailResponseDto orderDetailResponseDto = OrderDetailResponseDto.builder()
                .book(bookResponseDto)
                .quantity(1L)
                .wraps(List.of(wrapDto))
                .build();

        when(ordersRepository.findById("orderId")).thenReturn(Optional.of(order));
        when(orderDetailRepository.findByOrder(order)).thenReturn(Collections.singletonList(orderDetail));

        List<OrderDetailResponseDto> result = orderDetailService.getOrderDetailsByOrderId("orderId");

        assertNotNull(result);
        assertEquals(1, result.size());
        OrderDetailResponseDto responseDto = result.get(0);
        assertEquals("isbn", responseDto.getBook().getBookIsbn());
        assertEquals(1L, result.get(0).getQuantity());
    }

    @Test
    void getOrderDetailsByOrderId_WhenOrderNotFound_ThrowNotFoundOrderException() {

        String orderId = "nonExistingOrderId";
        when(ordersRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundOrderException.class, () -> {
            orderDetailService.getOrderDetailsByOrderId(orderId);
        });
    }
}
