package com.nhnacademy.shop.cart.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException() {
        super("구매하고자 하는 수량보다 도서 재고 수량이 부족하여 장바구니에 담을 수 없습니다.");
    }
}
