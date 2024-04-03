package com.nhnacademy.shop.address.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressModifyRequestDto {
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
