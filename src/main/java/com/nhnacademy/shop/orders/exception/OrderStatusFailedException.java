package com.nhnacademy.delivery.orders.exception;

public class OrderStatusFailedException extends RuntimeException{
    public OrderStatusFailedException(String orderState){
        super("invaild orderstate "+ orderState);
    }
}
