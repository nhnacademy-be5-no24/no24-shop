package com.nhnacademy.shop.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDeleteRequestDto {
    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("address_id")
    private Long addressId;
}
