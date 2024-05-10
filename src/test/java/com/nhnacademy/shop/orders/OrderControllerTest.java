package com.nhnacademy.shop.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.bookcategory.domain.BookCategory;
import com.nhnacademy.shop.category.domain.Category;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.coupon.dto.response.CouponResponseDto;
import com.nhnacademy.shop.coupon.entity.Coupon;
import com.nhnacademy.shop.coupon_member.domain.CouponMember;
import com.nhnacademy.shop.coupon_member.dto.response.CouponMemberResponseDto;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.order_detail.domain.OrderDetail;
import com.nhnacademy.shop.order_detail.dto.OrderDetailDto;
import com.nhnacademy.shop.orders.controller.OrderController;
import com.nhnacademy.shop.orders.domain.Orders;
import com.nhnacademy.shop.orders.dto.request.CartPaymentRequestDto;
import com.nhnacademy.shop.orders.dto.request.OrdersCreateRequestResponseDto;
import com.nhnacademy.shop.orders.dto.response.CartPaymentResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersListForAdminResponseDto;
import com.nhnacademy.shop.orders.dto.response.OrdersResponseDto;
import com.nhnacademy.shop.orders.service.OrdersService;
import com.nhnacademy.shop.payment.domain.Payment;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.domain.WrapInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(OrderController.class)
@Import(
        {RedisConfig.class}
)
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
    private WrapInfo wrapInfo;
    private Address address;
    private Member member;
    private Grade grade;
    private BookCategory bookCategory;
    private Category category;
    private Coupon coupon;
    private CouponMember couponMember;
    private OrdersListForAdminResponseDto adminResponseDto;
    private OrdersListForAdminResponseDto adminResponseDto2;
    private OrdersResponseDto ordersResponseDto;
    private OrdersResponseDto ordersResponseDto2;
    private OrdersCreateRequestResponseDto createRequestDto;
    private OrdersCreateRequestResponseDto createRequestDtoNoState;
    private CartPaymentRequestDto cartPaymentRequestDto;
    private CouponResponseDto couponResponseDto;
    private CartPaymentResponseDto cartPaymentResponseDto;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(ordersService)).build();
        MockitoAnnotations.openMocks(this);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        initializeEntities();
        initializeResponseDto();
    }

    private void initializeEntities() {
        payment = Payment.builder()
                .paymentId(1L).paymentName("name").build();

        customer = Customer.builder()
                .customerNo(1L)
                .customerId("id")
                .customerPassword("password")
                .customerName("name")
                .customerPhoneNumber("phoneNumber")
                .customerEmail("email")
                .customerBirthday(LocalDate.of(2024, 4, 14))
                .customerRole("role")
                .build();

        book = Book.builder()
                .bookIsbn("bookIsbn")
                .bookTitle("bookTitle")
                .bookDesc("desc")
                .bookPublisher("publisher")
                .bookPublishedAt(LocalDate.of(2024, 4, 14))
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
                .wrapName("name")
                .wrapCost(1L)
                .build();

        order = Orders.builder()
                .orderId("orderId")
                .orderDate(LocalDateTime.of(2024, 4, 14, 20, 10, 29))
                .shipDate(LocalDate.now())
                .deliveryFee(1)
                .totalFee(1L)
                .orderState(Orders.OrderState.WAITING)
                .payment(payment)
                .customer(customer)
                .receiverName("name")
                .receiverPhoneNumber("number")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("addressDetail")
                .build();

        orderDetail = OrderDetail.builder()
                .orderDetailId(1L)
                .book(book)
                .order(order)
                .amount(1L)
                .build();

        wrapInfo = WrapInfo.builder()
                .pk(new WrapInfo.Pk(wrap.getWrapId(), orderDetail.getOrderDetailId()))
                .wrap(wrap)
                .orderDetail(orderDetail)
                .amount(2L)
                .build();
        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("gradeName")
                .accumulateRate(1L)
                .build();
        member = Member.builder()
                .customerNo(1L)
                .customer(customer)
                .memberId("memberId")
                .lastLoginAt(LocalDateTime.now())
                .grade(grade)
                .role("role")
                .memberState(Member.MemberState.ACTIVE)
                .build();
        address = Address.builder()
                .addressId(1L)
                .alias("alias")
                .receiverName("name")
                .receiverPhoneNumber("phoneNumber")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("addressDetail")
                .isDefault(Boolean.TRUE)
                .member(member)
                .build();
        category = Category.builder()
                .categoryId(1L)
                .categoryName("categoryName")
                .parentCategory(null)
                .build();
        bookCategory = BookCategory.builder()
                .pk(new BookCategory.Pk("bookIsbn", 1L))
                .book(book)
                .category(category)
                .build();
        coupon = Coupon.builder()
                .couponId(1L)
                .couponName("couponName")
                .deadline(LocalDate.now())
                .issueLimit(1)
                .expirationPeriod(1)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .build();
        couponMember = CouponMember.builder()
                .couponMemberId(1L)
                .coupon(coupon)
                .member(member)
                .used(Boolean.FALSE)
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now())
                .usedAt(null)
                .status(CouponMember.Status.ACTIVE)
                .build();
    }
    private void initializeResponseDto() {

        OrderDetailDto.WrapDto wrapDto = OrderDetailDto.WrapDto.builder()
                .wrapId(1L)
                .quantity(1L)
                .build();
        OrderDetailDto orderDetailDto = OrderDetailDto.builder()
                .bookIsbn(book.getBookIsbn())
                .quantity(1L)
                .price(1L)
                .couponId(1L)
                .wraps(List.of(wrapDto))
                .build();
        List<OrderDetailDto> orderDetailDtoList = List.of(orderDetailDto);
        createRequestDto = OrdersCreateRequestResponseDto.builder()
                .orderId(order.getOrderId())
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
                .orderState(Orders.OrderState.COMPLETE_PAYMENT)
                .totalFee(1L)
                .deliveryFee(1)
                .paymentId(payment.getPaymentId())
                .customerNo(customer.getCustomerNo())
                .jSessionId("jSessionId")
                .receiverName("receiverName")
                .receiverPhoneNumber("phoneNumber")
                .zipcode("zipcode")
                .address(order.getAddress())
                .addressDetail(order.getAddressDetail())
                .req(order.getReq())
                .orderDetailDtoList(orderDetailDtoList)
                .build();


        adminResponseDto = OrdersListForAdminResponseDto.builder()
                .orderId("orderId")
                .customerName("customerName")
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
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
                .orderDate(LocalDateTime.now())
                .shipDate(LocalDate.now())
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
                LocalDateTime.now(),
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
                LocalDateTime.now(),
                "receiverName",
                "phoneNumber",
                "address",
                "addressDetail",
                Orders.OrderState.COMPLETED);

        couponResponseDto = CouponResponseDto.builder()
                .couponId(1L)
                .couponName("couponName")
                .issueLimit(1)
                .couponStatus(Coupon.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .bookIsbn("bookIsbn")
                .categoryId(1L)
                .discountPrice(1L)
                .discountRate(null)
                .maxDiscountPrice(2L)
                .build();

        CartPaymentRequestDto.BookInfo bookInfo = CartPaymentRequestDto.BookInfo.builder()
                .bookIsbn("bookIsbn")
                .bookSalePrice(1L)
                .quantity(1L)
                .build();
        List<CartPaymentRequestDto.BookInfo> bookInfoList = List.of(bookInfo);
        cartPaymentRequestDto = CartPaymentRequestDto.builder()
                .bookInfos(bookInfoList)
                .customerNo(1L)
                .build();
        CouponMemberResponseDto couponMemberResponseDto = CouponMemberResponseDto.builder()
                .couponMemberId(1L)
                .couponId(1L)
                .customerNo(1L)
                .couponName("couponName")
                .createdAt(LocalDateTime.now())
                .destroyedAt(LocalDateTime.now())
                .usedAt(null)
                .used(Boolean.FALSE)
                .couponStatus(CouponMember.Status.ACTIVE)
                .couponType(Coupon.CouponType.AMOUNT)
                .couponTarget(Coupon.CouponTarget.BOOK)
                .bookIsbn("bookIsbn")
                .categoryId(1L)
                .discountPrice(0L)
                .discountRate(null)
                .maxDiscountPrice(0L)
                .build();
        CartPaymentResponseDto.BookInfo responseBookInfo = CartPaymentResponseDto.BookInfo.builder()
                .bookIsbn("bookIsbn")
                .bookTitle("bookTitle")
                .bookSalePrice(1L)
                .quantity(1L)
                .coupons(List.of(couponMemberResponseDto))
                .wraps(List.of(wrap))
                .build();
        cartPaymentResponseDto = CartPaymentResponseDto.builder()
                .bookInfos(List.of(responseBookInfo))
                .totalPrice(1L)
                .customerNo(1L)
                .customerName("customerName")
                .customerPhoneNumber("phoneNumber")
                .customerEmail("email")
                .receiverName("receiverName")
                .receiverPhoneNumber("phoneNumber")
                .zipcode("zipcode")
                .address("address")
                .addressDetail("adressDetail")
                .req("req")
                .build();

    }
    @Test
    @DisplayName("admin 모든 주문 반환 test")
    void testGetOrders() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<OrdersListForAdminResponseDto> ordersList = List.of(adminResponseDto, adminResponseDto2);
        Page<OrdersListForAdminResponseDto> mockedPage = new PageImpl<>(ordersList, pageable, ordersList.size());

        when(ordersService.getOrders(any())).thenReturn(mockedPage);

        mockMvc.perform(get("/shop/orders/admin")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    @Disabled
    @DisplayName("고객 번호로 고객의 모든 주문 반환 test")
    void testGetOrdersByCustomer() throws Exception {
        List<OrdersResponseDto> mockedResponse = Arrays.asList(ordersResponseDto, ordersResponseDto2);
        Page<OrdersResponseDto> mockedPage = new PageImpl<>(mockedResponse);

        when(ordersService.getOrderByCustomer(any(), anyLong())).thenReturn(mockedPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/orders/customer/{customerNo}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("주문 아이디로 주문 정보 반환 test")
    void testGetOrderDetailByOrderId() throws Exception {

        when(ordersService.getOrderByOrdersId(anyString())).thenReturn(ordersResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/shop/orders/orderId/{orderId}", "orderId"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId", is(ordersResponseDto.getOrderId())));
    }
    @Test
    @Disabled
    @DisplayName("주문결제페이지 test")
    void testGetCartPaymentInfo() throws  Exception{
        when(ordersService.getCartPaymentInfo(any()))
                .thenReturn(cartPaymentResponseDto);

        mockMvc.perform(post("/shop/orders/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPrice").value(1))
                // Add more assertions if needed
                .andExpect(jsonPath("$.bookInfos[0].bookTitle").value("bookTitle"));
    }



    @Test
    @DisplayName("주문 상태 수정 성공 test")
    void testModifyOrderState() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createRequestDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/shop/orders/{orderId}/state", "orderId")
                        .param("orderState", Orders.OrderState.SHIPPING.name()))
                .andExpect(status().isCreated());
    }

}
