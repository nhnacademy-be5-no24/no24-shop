package com.nhnacademy.shop.address.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NonMemberAddressResponseDto {
    private Long customerNo;
    private Long orderId;
}
