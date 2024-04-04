package com.nhnacademy.shop.address.exception;

/**
 * 어떤 멤버의 주소가 10개를 초과했을 때 발생하는 Exception
 *
 * @Author: jinjiwon
 * @Date: 04/04/2023
 */
public class AddressOutOfBoundsException extends RuntimeException {
    public AddressOutOfBoundsException() {
        super("주소가 10개를 초과하여 등록이 불가능합니다.");
    }
}
