package com.nhnacademy.shop.cart.controller;

import com.nhnacademy.shop.cart.dto.request.CartRequestDto;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;
import com.nhnacademy.shop.cart.service.CartService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 장바구니 조회
    @GetMapping("/{customerNo}")
    public ResponseEntity<Page<CartResponseDto>> getCarts(@PathVariable Long customerNo, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<CartResponseDto> carts = cartService.getCarts(customerNo, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }

    // 장바구니 등록
    @PostMapping("/create")
    public ResponseEntity<CartResponseDto> saveCart(@RequestBody CartRequestDto cartRequestDto) {
        CartResponseDto cartResponseDto = cartService.saveCart(cartRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponseDto);
    }

    // 장바구니 수정
    @PutMapping("/modify/{cartId}")
    public CartResponseDto modifyCart(@PathVariable Long cartId, @RequestBody CartRequestDto cartRequestDto) {
        return cartService.modifyCart(cartId, cartRequestDto);
    }

    // 장바구니 삭제
    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

