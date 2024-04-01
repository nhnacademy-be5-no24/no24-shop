package com.nhnacademy.shop.publisher.dto;

import com.nhnacademy.shop.publisher.domain.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto for PublisherResponse
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherResponseDto {
    Long publisherId;
    String publisherName;

    public PublisherResponseDto(Publisher publisher) {
        this.publisherId = publisher.getPublisherId();
        this.publisherName = publisher.getPublisherName();
    }
}
