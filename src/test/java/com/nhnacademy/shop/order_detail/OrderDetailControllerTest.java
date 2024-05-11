package com.nhnacademy.shop.order_detail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.order_detail.controller.OrderDetailController;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.order_detail.dto.OrderDetailResponseDto;
import com.nhnacademy.shop.order_detail.service.OrderDetailService;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.tag.dto.TagResponseDto;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderDetailController.class)
@Import(
        {RedisConfig.class}
)
class OrderDetailControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private OrderDetailService orderDetailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderDetailController(orderDetailService)).build();

    }

    @Test
    void getOrderDetailsTest() throws Exception{
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

        List<OrderDetailResponseDto> dtos = List.of(orderDetailResponseDto);
        String orderId = "orderId";


        when(orderDetailService.getOrderDetailsByOrderId(anyString())).thenReturn(dtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/orders/detail/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));


    }
}
