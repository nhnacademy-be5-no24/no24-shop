package com.nhnacademy.shop.publisher.service;

import com.nhnacademy.shop.publisher.dto.PublisherRequestDto;
import com.nhnacademy.shop.publisher.dto.PublisherResponseDto;

/**
 * Publisher service
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
public interface PublisherService {
    PublisherResponseDto getPublisherById(Long publisherId);
    PublisherResponseDto getPublisherByName(String publisherName);
    PublisherResponseDto savePublisher(PublisherRequestDto publisherRequestDto);
    PublisherResponseDto modifyPublisher(Long publisherId, PublisherRequestDto publisherRequestDto);
    void deletePublisherByName(String publisherName);
    void deletePublisherById(Long publisherId);
}
