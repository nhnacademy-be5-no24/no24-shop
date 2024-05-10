package com.nhnacademy.shop.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Payment 전달을 위한 dto
 *
 * @Author : 박병휘
 * @Date : 2024/04/25
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private String paymentName;
}
