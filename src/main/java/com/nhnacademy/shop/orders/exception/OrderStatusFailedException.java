package com.nhnacademy.shop.orders.exception;

public class OrderStatusFailedException extends RuntimeException{
    public OrderStatusFailedException(String orderState){
        super("invaild orderstate "+ orderState);
    }
}
