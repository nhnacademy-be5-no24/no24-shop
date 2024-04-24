package com.nhnacademy.shop.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.cart.domain.Cart;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;
import com.nhnacademy.shop.cart.exception.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cart Controller
 *
 * @Author: jinjiwon
 * @Date: 17/04/2024
 */
@RestController
@RequestMapping("/shop/cart")
@RequiredArgsConstructor
public class CartController {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final BookRepository bookRepository;

    /**
     *  [GET /shop/cart/{customerId}
     *  장바구니를 조회하는 Get 메서드
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<Page<CartResponseDto>> getCarts(@PathVariable String customerId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        Cart cart = hashOperations.get("cart", customerId);

        List<CartResponseDto> cartResponseDtoList = new ArrayList<>();

        if (cart != null) {
            for (Cart.Book book : cart.getBooks()) {
                Optional<Book> foundBookByIsbn = bookRepository.findByBookIsbn(book.getIsbn());

                CartResponseDto cartResponseDto = CartResponseDto.builder()
                        .bookIsbn(foundBookByIsbn.get().getBookIsbn())
                        .bookTitle(foundBookByIsbn.get().getBookTitle())
                        .bookFixedPrice(foundBookByIsbn.get().getBookFixedPrice())
                        .bookSalePrice(foundBookByIsbn.get().getBookSalePrice())
                        .bookStatus(foundBookByIsbn.get().getBookStatus())
                        .bookQuantity(foundBookByIsbn.get().getBookQuantity())
                        .bookImage(foundBookByIsbn.get().getBookImage())
                        .build();

                cartResponseDtoList.add(cartResponseDto);
            }
        } else {
            throw new CartNotFoundException(customerId + "의 장바구니를 찾을 수 없습니다.");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), cartResponseDtoList.size());
        Page<CartResponseDto> carts = new PageImpl<>(cartResponseDtoList.subList(start, end), pageRequest, cartResponseDtoList.size());

        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }
}
