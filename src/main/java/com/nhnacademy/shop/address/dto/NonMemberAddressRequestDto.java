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
public class NonMemberAddressRequestDto {
    @JsonProperty("customer_no")
    private Long customerNo;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("address_id")
    private Long addressId;

    private String alias;

    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("receiver_phone_number")
    private String receiverPhoneNumber;

    private String zipcode;

    private String address;

    @JsonProperty("address_detail")
    private String addressDetail;

    @JsonProperty("is_default")
    private Boolean isDefault;
}
