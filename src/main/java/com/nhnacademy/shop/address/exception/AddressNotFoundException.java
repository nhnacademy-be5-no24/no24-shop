package com.nhnacademy.shop.address.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long addressId) {
        super(addressId + "가 존재하지 않습니다.");
    }
}
