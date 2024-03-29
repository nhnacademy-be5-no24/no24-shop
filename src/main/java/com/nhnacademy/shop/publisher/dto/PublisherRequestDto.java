package com.nhnacademy.shop.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto for PublisherRequest
 *
 * @Author : 박병휘
 * @Date : 2024/03/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherRequestDto {
    String publisherName;
}
