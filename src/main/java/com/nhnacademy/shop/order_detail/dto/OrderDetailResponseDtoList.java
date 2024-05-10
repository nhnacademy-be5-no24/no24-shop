package com.nhnacademy.shop.order_detail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/05/06
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDtoList {
    private List<OrderDetailResponseDto> orderDetailResponseDtoList;
}
