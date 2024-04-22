package com.nhnacademy.shop.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.orders.controller.OrderController;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.dto.request.OrdersCreateRequestDto;
import com.nhnacademy.shop.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.service.OrdersService;
import com.nhnacademy.shop.payment.domain.Payment;
import com.nhnacademy.shop.wrap.domain.Wrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    private MockMvc mockMvc;
    @MockBean
    private OrdersService ordersService;
    @MockBean
    EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Customer customer;
    private Book book;
    private Wrap wrap;
    private Orders order;
    private OrderDetail orderDetail;
    private Payment payment;
    private OrdersListForAdminResponseDto adminResponseDto;
    private OrdersListForAdminResponseDto adminResponseDto2;
    private OrdersResponseDto ordersResponseDto;
    private OrdersResponseDto ordersResponseDto2;
    private OrdersCreateRequestDto createRequestDto;
    private OrdersCreateRequestDto createRequestDtoNoState;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(ordersService)).build();
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        initializeEntities();
        initializeAdminResponseDto();
    }

    private void initializeEntities() {
        payment = Payment.builder()
                .paymentId(1L).paymentName("paymentName").build();

        customer = Customer.builder()
                .customerNo(1L)
                .customerId("customerId")
                .customerPassword("customerPassword")
                .customerName("customerName")
                .customerPhoneNumber("customerPhoneNumber")
                .customerEmail("customerEmail")
                .customerBirthday(LocalDate.of(2024, 4, 15))
                .customerRole("user")
                .build();

        book = Book.builder()
                .bookIsbn("Isbn")
                .bookTitle("Title")
                .bookDesc("desc")
                .bookPublisher("publisher")
                .bookPublishedAt(LocalDate.of(2024, 4, 15))
                .bookFixedPrice(1L)
                .bookSalePrice(1L)
                .bookIsPacking(true)
                .bookViews(1L)
                .bookStatus(0)
                .bookQuantity(1)
                .bookImage("image")
                .build();

        wrap = Wrap.builder()
                .wrapId(1L)
                .wrapName("wrapName")
                .wrapCost(1L)
                .build();

        order = Orders.builder()
                .orderId("orderId")
                .orderDate(LocalDate.of(2024, 4, 15))
                .deliveryFee(1L)
                .orderState(Orders.OrderState.WAITING)
                .payment(payment)
                .customer(customer)
                .receiverName("receiverName")
                .receiverPhoneNumber("receiverPhoneNumber")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("addressDetail")
                .build();

        orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .book(book)
                .wrap(wrap)
                .order(order)
                .build();

    }
    private void initializeAdminResponseDto() {
        createRequestDto = new OrdersCreateRequestDto(
                LocalDate.of(2024, 4, 15),
                Orders.OrderState.COMPLETE_PAYMENT,
                1L,
                payment,
                customer,
                "receiverName",
                "receiverPhoneNumber",
                "zipcode",
                "address",
                "addressDetail",
                "req",
                Collections.singletonList(orderDetail)
        );
        createRequestDtoNoState = new OrdersCreateRequestDto(
                LocalDate.of(2024, 4, 15),
                Orders.OrderState.SHIPPING,
                1L,
                payment,
                customer,
                "receiverName",
                "receiverPhoneNumber",
                "zipcode",
                "address",
                "addressDetail",
                "req",
                Collections.singletonList(orderDetail)
        );

        adminResponseDto = OrdersListForAdminResponseDto.builder()
                .orderId("orderId")
                .customerName("customerName")
                .orderDate(LocalDate.of(2024,4,15))
                .orderState(Orders.OrderState.WAITING)
                .address("address")
                .addressDetail("addressDetail")
                .wrapName("wrapName")
                .wrapCost(1L)
                .bookTitle("bookTittle")
                .bookSalePrice(1L).build();
        adminResponseDto2 = OrdersListForAdminResponseDto.builder()
                .orderId("orderId2")
                .customerName("customerName2")
                .orderDate(LocalDate.of(2024,4,15))
                .orderState(Orders.OrderState.WAITING)
                .address("address")
                .addressDetail("addressDetail")
                .wrapName("wrapName")
                .wrapCost(1L)
                .bookTitle("bookTittle")
                .bookSalePrice(1L).build();

        ordersResponseDto = new OrdersResponseDto("orderId",
                "bookTitle",
                1L,
                "wrapName",
                1L,
                LocalDate.of(2024, 4, 15),
                "receiverName",
                "phoneNumber",
                "address",
                "addressDetail",
                Orders.OrderState.WAITING);
        ordersResponseDto2 = new OrdersResponseDto("orderId2",
                "bookTitle",
                1L,
                "wrapName",
                1L,
                LocalDate.of(2024, 4, 15),
                "receiverName",
                "phoneNumber",
                "address",
                "addressDetail",
                Orders.OrderState.COMPLETED);


    }
    @Test
    @DisplayName("admin 모든 주문 반환 test")
    void testGetOrders() throws Exception {

        List<OrdersListForAdminResponseDto> ordersList = Arrays.asList(adminResponseDto, adminResponseDto2);
        Page<OrdersListForAdminResponseDto> mockedPage = new PageImpl<>(ordersList, PageRequest.of(0,10), 2);

        when(ordersService.getOrders(any())).thenReturn(mockedPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].orderId", is("orderId")))
                .andExpect(jsonPath("$.content[0].customerName", is("customerName")))
                .andExpect(jsonPath("$.content[1].orderId", is("orderId2")))
                .andExpect(jsonPath("$.content[1].customerName", is("customerName2")));
    }
    @Test
    @DisplayName("고객 번호로 고객의 모든 주문 반환 test")
    void testGetOrdersByCustomer() throws Exception {
        List<OrdersResponseDto> mockedResponse = Arrays.asList(ordersResponseDto, ordersResponseDto2);
        Page<OrdersResponseDto> mockedPage = new PageImpl<>(mockedResponse);

        when(ordersService.getOrderByCustomer(any(), anyLong())).thenReturn(mockedPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/customer/{customerNo}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].orderId", is("orderId")))
                .andExpect(jsonPath("$.content[0].bookTitle", is("bookTitle")));
    }

    @Test
    @DisplayName("주문 아이디로 주문 정보 반환 test")
    void testGetOrderDetailByOrderId() throws Exception {

        when(ordersService.getOrderByOrdersId(anyString())).thenReturn(ordersResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/orderId/{orderId}", "orderId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId", is(ordersResponseDto.getOrderId())));
    }
    @Test
    @DisplayName("주문 생성 성공 test")
    void testCreateOrder() throws  Exception{
        String jsonRequest = objectMapper.writeValueAsString(createRequestDto);
        when(ordersService.createOrder(createRequestDto)).thenReturn(ordersResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문의 타입을 JSON으로 설정
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("주문 상태 수정 성공 test")
    void testModifyOrderState() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/{orderId}/state", "orderId")
                        .param("orderState", Orders.OrderState.SHIPPING.name()))
                .andExpect(status().isCreated());
    }
}
