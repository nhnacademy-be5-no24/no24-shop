package com.nhnacademy.delivery.orders.exception;

public class NotFoundOrderException extends RuntimeException{
    public NotFoundOrderException(String orderId){
        super("not found "+orderId);
    }
}
