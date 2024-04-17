package com.nhnacademy.shop.address.service.impl;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.dto.request.AddressCreateRequestDto;
import com.nhnacademy.shop.address.dto.request.AddressModifyRequestDto;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;
import com.nhnacademy.shop.address.exception.AddressOutOfBoundsException;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.address.service.AddressService;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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
    private final MemberRepository memberRepository;

    // 주소 조회
    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddresses(Long customerNo) {
        List<Address> addresses = addressRepository.findByMemberCustomerNo(customerNo);
        return AddressMapper.addressToAddressResponseDtoList(addresses);
    }

    // 주소 등록
    @Override
    @Transactional
    public AddressResponseDto saveAddress(AddressCreateRequestDto addressCreateRequestDto) {
        List<Address> addresses = addressRepository.findByMemberCustomerNo(addressCreateRequestDto.getCustomerNo());

        if (addresses.size() >= 10) {
            throw new AddressOutOfBoundsException();
        } else {
            Member member = memberRepository.findMemberByCustomerNo(addressCreateRequestDto.getCustomerNo());

            Address newAddress = Address.builder()
                    .alias(addressCreateRequestDto.getAlias())
                    .receiverName(addressCreateRequestDto.getReceiverName())
                    .receiverPhoneNumber(addressCreateRequestDto.getReceiverPhoneNumber())
                    .zipcode(addressCreateRequestDto.getZipcode())
                    .address(addressCreateRequestDto.getAddress())
                    .addressDetail(addressCreateRequestDto.getAddressDetail())
                    .isDefault(addressCreateRequestDto.getIsDefault())
                    .member(member)
                    .build();

            // 주소 등록 시, 기본 설정(is_default) 처리
            boolean updateAddressIsDefaultCheck = addressCreateRequestDto.getIsDefault();

            if (updateAddressIsDefaultCheck) {
                for (Address address : addresses) {
                    boolean addressIsDefaultCheck = address.getIsDefault();
                    if (addressIsDefaultCheck) {
                        address.setIsDefault(false);
                    }
                }
            }

            addressRepository.save(newAddress);

            return AddressMapper.addressToAddressResponseDto(newAddress);
        }
    }

    // 주소 수정
    @Override
    @Transactional
    public AddressResponseDto modifyAddress(Long addressId, AddressModifyRequestDto addressModifyRequestDto) {
        Address originAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        Address updatedAddress = Address.builder()
                .addressId(originAddress.getAddressId())
                .alias(addressModifyRequestDto.getAlias())
                .receiverName(addressModifyRequestDto.getReceiverName())
                .receiverPhoneNumber(addressModifyRequestDto.getReceiverPhoneNumber())
                .zipcode(addressModifyRequestDto.getZipcode())
                .address(addressModifyRequestDto.getAddress())
                .addressDetail(addressModifyRequestDto.getAddressDetail())
                .isDefault(addressModifyRequestDto.getIsDefault())
                .member(originAddress.getMember())
                .build();

        // 주소 수정 시, 기본 설정(is_default) 처리
        List<Address> addresses = addressRepository.findByMemberCustomerNo(originAddress.getMember().getCustomerNo());

        boolean modifyRequestDtoIsDefaultCheck = addressModifyRequestDto.getIsDefault();

        if (modifyRequestDtoIsDefaultCheck) {
            for (Address address : addresses) {
                boolean addressIsDefaultCheck = address.getIsDefault();
                if (addressIsDefaultCheck) {
                    address.setIsDefault(false);
                }
            }
        }

        addressRepository.save(updatedAddress);

        return AddressMapper.addressToAddressResponseDto(updatedAddress);
    }

    // 주소 삭제
    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }
}
