package com.nhnacademy.shop.publisher.exception;

/**
 * NotFoundPublisher Exception
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
public class NotFoundPublisherException extends RuntimeException {
    public NotFoundPublisherException(Long publisherId) {
        super(publisherId + "가 존재하지 않습니다.");
    }
}
