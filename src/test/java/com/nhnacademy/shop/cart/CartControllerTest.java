package com.nhnacademy.shop.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.cart.controller.CartController;
import com.nhnacademy.shop.cart.domain.Cart;
import com.nhnacademy.shop.cart.dto.request.CartRequestDto;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;
import com.nhnacademy.shop.cart.exception.CartNotFoundException;
import com.nhnacademy.shop.cartOrder.domain.CartOrder;
import com.nhnacademy.shop.config.RedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Cart RestController Test
 *
 * @Author : jinjiwon
 * @Date : 10/05/2024
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CartController.class)
@ActiveProfiles("dev")
@Import({RedisConfig.class})
class CartControllerTest {
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    RedisTemplate<String, Object> redisTemplate;
    @MockBean
    BookRepository bookRepository;
    Pageable pageable;
    Integer pageSize;
    Integer offset;
    Book book;
    CartRequestDto cartRequestDto;
    CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        redisTemplate = Mockito.mock(RedisTemplate.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CartController(redisTemplate, bookRepository))
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        pageSize = 0;
        offset = 10;
        pageable = PageRequest.of(pageSize, offset);

        book = Book.builder()
                .bookIsbn("qwer1234")
                .bookTitle("no24")
                .bookDesc("description")
                .bookPublisher("nhn")
                .bookPublishedAt(LocalDate.parse("2024-05-10"))
                .bookFixedPrice(20000)
                .bookSalePrice(17000)
                .bookIsPacking(true)
                .bookViews(20L)
                .bookStatus(0)
                .bookQuantity(40)
                .bookImage("no24.jpg")
                .build();

        cartRequestDto = CartRequestDto.builder()
                .bookIsbn(book.getBookIsbn())
                .bookQuantity(2)
                .build();

        cartResponseDto = CartResponseDto.builder()
                .bookIsbn(book.getBookIsbn())
                .bookTitle(book.getBookTitle())
                .bookFixedPrice(book.getBookFixedPrice())
                .bookSalePrice(book.getBookSalePrice())
                .bookStatus(book.getBookStatus())
                .bookQuantity(cartRequestDto.getBookQuantity())
                .bookImage(book.getBookImage())
                .build();
    }

    @Test
    @DisplayName("장바구니 등록 - 성공")
    void testAddToCart() throws Exception {
        String customerNo = "1";
        HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(null);
        doNothing().when(hashOperations).put(anyString(), any(), any());

        mockMvc.perform(post("/shop/cart/create/{customerNo}", customerNo)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(cartRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 조회 - 성공")
    void testGetCarts_Success() throws Exception {
        String customerNo = "1";
        Cart cart = new Cart();
        List<Cart.Book> books = new ArrayList<>();
        Cart.Book book1 = new Cart.Book("qwer1234", 2);
        books.add(book1);
        cart.setCustomerNo(customerNo);
        cart.setBooks(books);

        HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(cart);
        when(bookRepository.findByBookIsbn(anyString())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/shop/cart/{customerNo}", customerNo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 상품 수량 변경 - 성공")
    void testUpdateCart_Success() throws Exception {
        String customerNo = "1";
        Cart cart = new Cart();
        List<Cart.Book> books = new ArrayList<>();
        Cart.Book book1 = new Cart.Book("qwer1234", 2);
        books.add(book1);
        cart.setCustomerNo(customerNo);
        cart.setBooks(books);

        HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(any(), any())).thenReturn(cart);
        doNothing().when(hashOperations).put(any(), any(), any());

        mockMvc.perform(put("/shop/cart/update/{customerNo}", customerNo)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(cartRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장바구니 상품 삭제(단건) - 성공")
    void testDeleteCartBook_Success() throws Exception {
        String customerNo = "1";
        String bookIsbn = "qwer1234";

        Cart cart = new Cart();
        List<Cart.Book> books = new ArrayList<>();
        Cart.Book book1 = new Cart.Book(bookIsbn, 2);
        books.add(book1);
        cart.setCustomerNo(customerNo);
        cart.setBooks(books);

        HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.hasKey(any(), any())).thenReturn(true);
        when(hashOperations.get(any(), any())).thenReturn(cart);
        doNothing().when(hashOperations).put(any(), any(), any());

        mockMvc.perform(delete("/shop/cart/deleteOne/{customerNo}", customerNo)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .param("bookIsbn", bookIsbn))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("결제 완료 후, 구매한 상품 삭제")
    void testUpdateCartValue_Success() throws Exception {
        String customerNo = "1";
        Cart cart = new Cart();
        List<Cart.Book> books = new ArrayList<>();
        Cart.Book book1 = new Cart.Book("qwer1234", 2);
        books.add(book1);
        cart.setCustomerNo(customerNo);
        cart.setBooks(books);

        CartOrder cartOrder = new CartOrder();
        List<CartOrder.Book> books_cartOrder = new ArrayList<>();
        CartOrder.Book book2 = new CartOrder.Book("qwer1234", 1);
        books_cartOrder.add(book2);
        cartOrder.setCustomerId(customerNo);
        cartOrder.setBooks(books_cartOrder);

        HashOperations<String, Object, Object> hashOperations = mock(HashOperations.class);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(eq("cart"), any())).thenReturn(cart);
        when(hashOperations.get(eq("order"), any())).thenReturn(cartOrder);
        doNothing().when(hashOperations).put(eq("cart"), eq(customerNo), any());

        mockMvc.perform(put("/shop/cart/{customerNo}", customerNo)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }
}
