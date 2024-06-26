package com.nhnacademy.shop.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.shop.book.entity.Book;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.cart.domain.Cart;
import com.nhnacademy.shop.cart.dto.request.CartRequestDto;
import com.nhnacademy.shop.cart.dto.response.CartListResponseDto;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;
import com.nhnacademy.shop.cart.exception.CartNotFoundException;
import com.nhnacademy.shop.cartOrder.domain.CartOrder;
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
     *  [GET /shop/cart/{customerNo}
     *  장바구니를 조회하는 Get 메서드
     */
    @GetMapping("/{customerNo}")
    public ResponseEntity<CartListResponseDto> getCarts(@PathVariable String customerNo,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        Cart cart = hashOperations.get("cart", customerNo);

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
                        .bookQuantity(book.getQuantity())
                        .bookImage(foundBookByIsbn.get().getBookImage())
                        .build();

                cartResponseDtoList.add(cartResponseDto);
            }
        } else {
            throw new CartNotFoundException(customerNo);
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), cartResponseDtoList.size());
        Page<CartResponseDto> carts = new PageImpl<>(cartResponseDtoList.subList(start, end), pageRequest, cartResponseDtoList.size());

        CartListResponseDto cartListResponseDto = new CartListResponseDto(carts.toList());

        return ResponseEntity.status(HttpStatus.OK).body(cartListResponseDto);
    }

    /**
     * [POST /shop/cart/create]
     * 장바구니에 상품을 추가하는 Post 메소드
     */
    // todo: 비회원 시, redis 유효시간 3시간 설정 추가
    @PostMapping("/create/{customerNo}")
    public ResponseEntity<String> addToCart(@PathVariable("customerNo") String customerNo, @RequestBody CartRequestDto cartRequestDto) {
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        Cart cart = hashOperations.get("cart", customerNo);

        // 고객의 장바구니가 존재하는지 확인
        if (cart == null) {
            cart = new Cart();
            cart.setCustomerNo(customerNo);
        }

        // 장바구니에 동일한 상품이 있는지 체크
        boolean isExistingItem = false;

        for (Cart.Book existingBook : cart.getBooks()) {
            if (existingBook.getIsbn().equals(cartRequestDto.getBookIsbn())) {
                existingBook.setQuantity(existingBook.getQuantity() + cartRequestDto.getBookQuantity());
                isExistingItem = true;
                break;
            }
        }

        if (!isExistingItem) {
            Cart.Book newBook = Cart.Book.builder()
                    .isbn(cartRequestDto.getBookIsbn())
                    .quantity(cartRequestDto.getBookQuantity())
                    .build();
            cart.getBooks().add(newBook);
        }

        hashOperations.put("cart", String.valueOf(customerNo), cart);

        return ResponseEntity.ok(customerNo + "의 장바구니에 " + cartRequestDto.getBookIsbn() + "이/가 " + cartRequestDto.getBookQuantity() + "개 추가되었습니다.");
    }

    /**
     * [PUT /shop/cart/update]
     * 사용자의 action으로 장바구니 상품 수량이 변경되는 경우
     */
    @PutMapping("/update/{customerNo}")
    public ResponseEntity<String> updateCart(@PathVariable String customerNo, @RequestBody CartRequestDto cartRequestDto) {
        String bookIsbn = cartRequestDto.getBookIsbn();
        int newQuantity = cartRequestDto.getBookQuantity();

        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        Cart cart = hashOperations.get("cart", customerNo);

        if (cart != null) {
            for (Cart.Book book : cart.getBooks()) {
                if (book.getIsbn().equals(bookIsbn)) {
                    book.setQuantity(newQuantity);
                    break;
                }
            }

            hashOperations.put("cart", String.valueOf(customerNo), cart);
            return ResponseEntity.ok("장바구니 상품 수량이 성공적으로 업데이트 되었습니다.");
        } else {
            throw new CartNotFoundException(customerNo);
        }
    }

    /**
     * [DELETE /shop/cart/deleteOne/{customerNo}]
     * 사용자의 action으로 장바구니 내 '하나'의 상품 삭제
     */
    @DeleteMapping("/deleteOne/{customerNo}")
    public ResponseEntity<String> deleteCartBook(@PathVariable String customerNo, @RequestParam String bookIsbn) {
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();

        if (hashOperations.hasKey("cart", customerNo)) {
            Cart cart = hashOperations.get("cart", customerNo);
            cart.getBooks().removeIf(book -> book.getIsbn().equals(bookIsbn));
            hashOperations.put("cart", String.valueOf(customerNo), cart);
            return ResponseEntity.ok(bookIsbn + "이 장바구니에서 삭제되었습니다.");
        } else {
            throw new CartNotFoundException(customerNo);
        }
    }

    /**
     * [PUT /shop/cart/{customerNo}]
     * 결제 완료 후, 장바구니에서 구매한 상품 제거
     */
    @PutMapping("/{customerNo}")
    public ResponseEntity<String> updateCartValue(@PathVariable String customerNo) {
        try {
            HashOperations<String, String, Cart> hashOperations_cart = redisTemplate.opsForHash();
            Cart cart = hashOperations_cart.get("cart", customerNo);

            HashOperations<String, String, CartOrder> hashOperations_order = redisTemplate.opsForHash();
            CartOrder order = hashOperations_order.get("order", customerNo);

            List<Cart.Book> updatedBooks = new ArrayList<>();
            for (Cart.Book cartBook : cart.getBooks()) {
                boolean found = false;
                for (CartOrder.Book orderBook : order.getBooks()) {
                    if (cartBook.getIsbn().equals(orderBook.getIsbn())) {
                        int newQuantity = cartBook.getQuantity() - orderBook.getQuantity();
                        if (newQuantity > 0) {
                            cartBook.setQuantity(newQuantity);
                            updatedBooks.add(cartBook);
                            found = true;
                        } else {
                            found = true;
                        }
                        break;
                    }
                }
                if (!found) {
                    updatedBooks.add(cartBook);
                }
            }

            Cart newCart = new Cart();
            newCart.setCustomerNo(customerNo);
            newCart.setBooks(updatedBooks);

            hashOperations_cart.put("cart", customerNo, newCart);

            return ResponseEntity.ok("Cart value updated successfully for customerId: " + customerNo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update cart value for customerId: " + customerNo);
        }
    }

    // todo: 수정 필요
    // 이건 {customerNo} 장바구니 자체가 삭제되는 코드 => 사용자가 전체 삭제 눌렀을 때 쓰면 될 듯!
    /**
     * [DELETE /shop/cart/deleteAll/{customerNo}]
     * customerId의 장바구니를 삭제하는 DELETE 메서드
     */
    @DeleteMapping("/deleteAll/{customerNo}")
    public ResponseEntity<String> deleteCart(@PathVariable String customerNo) {
        HashOperations<String, String, Cart> hashOperations = redisTemplate.opsForHash();
        hashOperations.hasKey("cart", customerNo);

        if (hashOperations.hasKey("cart", customerNo)) {
            hashOperations.delete("cart", customerNo);
            return ResponseEntity.ok(customerNo + "의 장바구니가 삭제되었습니다.");
        } else {
            throw new CartNotFoundException(customerNo);
        }
    }
}
