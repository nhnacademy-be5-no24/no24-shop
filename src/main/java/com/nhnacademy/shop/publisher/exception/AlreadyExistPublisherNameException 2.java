package com.nhnacademy.shop.publisher.exception;

/**
 * AlreadyExistPublisherName Exception
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
public class AlreadyExistPublisherNameException  extends RuntimeException {
    public AlreadyExistPublisherNameException(String PublisherName) {
        super(PublisherName + "가 이미 존재합니다.");
    }
}