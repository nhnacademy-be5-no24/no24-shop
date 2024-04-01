package com.nhnacademy.shop.address.service.impl;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.dto.request.AddressDeleteRequestDto;
import com.nhnacademy.shop.address.dto.request.AddressRequestDto;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * AddressService 구현체
 *
 * @Author : jinjiwon
 * @Date : 28/03/2024
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    // 주소 조회
    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddresses(Long customerNo) {
        List<Address> addresses = addressRepository.findByCustomerNo(customerNo);
        return AddressMapper.addressToAddresResponseDtoList(addresses);
    }

    /**
     * Entity와 DTO 간의 매핑을 담당하는 method
     *
     * @Author: jinjiwon
     * @Date : 28/03/2024
     */
    public static class AddressMapper {
        public static AddressResponseDto addressToAddresResponseDto(Address address) {
            return AddressResponseDto.builder()
                    .addressId(address.getAddressId())
                    .alias(address.getAlias())
                    .receiverName(address.getReceiverName())
                    .receiverPhoneNumber(address.getReceiverPhoneNumber())
                    .zipcode(address.getZipcode())
                    .address(address.getAddress())
                    .addressDetail(address.getAddressDeatail())
                    .isDefault(address.getIsDefault())
                    .build();
        }

        public static List<AddressResponseDto> addressToAddresResponseDtoList(List<Address> addresses) {
            return addresses.stream()
                    .map(AddressMapper::addressToAddresResponseDto)
                    .collect(Collectors.toList());
        }
    }
}
