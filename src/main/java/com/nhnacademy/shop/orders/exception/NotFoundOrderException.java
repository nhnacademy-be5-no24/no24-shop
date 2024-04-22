package com.nhnacademy.shop.orders.exception;

public class NotFoundOrderException extends RuntimeException{
    public NotFoundOrderException(String orderId){
        super("not found "+orderId);
    }
}
