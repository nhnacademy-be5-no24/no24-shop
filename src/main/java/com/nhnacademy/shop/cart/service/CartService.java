package com.nhnacademy.shop.cart.service;

import com.nhnacademy.shop.cart.dto.request.CartRequestDto;
import com.nhnacademy.shop.cart.dto.response.CartResponseDto;

import java.util.List;

public interface CartService {
    // 장바구니 조회
    List<CartResponseDto> getCart(Long customerNo);

    // 장바구니 등록
    CartResponseDto saveCart(CartRequestDto cartRequestDto);

    // 장바구니 수정
    CartResponseDto modifyCart(Long cartId, CartRequestDto cartRequestDto);

    // 장바구니 삭제
    void deleteCart(Long cartId);
}
