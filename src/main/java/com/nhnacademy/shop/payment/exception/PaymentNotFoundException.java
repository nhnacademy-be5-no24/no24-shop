package com.nhnacademy.shop.payment.exception;

/**
 * 결제 수단을 찾지 못했을 때 반환하는 exception.
 *
 * @author : 박병휘
 * @date : 2024-04-25
 */
public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super("해당 결제수단을 찾을 수 없습니다.");
    }
}
