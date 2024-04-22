package com.nhnacademy.shop.address.service.impl;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity와 DTO 간의 매핑을 담당하는 method
 *
 * @Author: jinjiwon
 * @Date : 28/03/2024
 */
public class AddressMapper {

    private AddressMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static AddressResponseDto addressToAddressResponseDto(Address address) {
        return AddressResponseDto.builder()
                .addressId(address.getAddressId())
                .alias(address.getAlias())
                .receiverName(address.getReceiverName())
                .receiverPhoneNumber(address.getReceiverPhoneNumber())
                .zipcode(address.getZipcode())
                .address(address.getAddress())
                .addressDetail(address.getAddressDetail())
                .isDefault(address.getIsDefault())
                .build();
    }

    public static List<AddressResponseDto> addressToAddressResponseDtoList(List<Address> addresses) {
        return addresses.stream()
                .map(AddressMapper::addressToAddressResponseDto)
                .collect(Collectors.toList());
    }
}
