package com.nhnacademy.shop.publisher.exception;

/**
 * NotFoundPublisherName Exception
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
public class NotFoundPublisherNameException extends RuntimeException {
    public NotFoundPublisherNameException(String PublisherName) {
        super(PublisherName + "가 존재하지 않습니다.");
    }
}
