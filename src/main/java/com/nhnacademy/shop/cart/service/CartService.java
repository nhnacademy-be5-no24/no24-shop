package com.nhnacademy.shop.cart.service;

import com.nhnacademy.shop.cart.dto.request.CartRequestDto;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;
import org.springframework.data.domain.Page;

public interface CartService {
    // 장바구니 조회
    Page<CartResponseDto> getCarts(Long customerNo, int page, int size);

    // 장바구니 등록
    CartResponseDto saveCart(CartRequestDto cartRequestDto);

    // 장바구니 수정
    CartResponseDto modifyCart(Long cartId, CartRequestDto cartRequestDto);

    // 장바구니 삭제
    void deleteCart(Long cartId);
}
