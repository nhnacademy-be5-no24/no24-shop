package com.nhnacademy.shop.address;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.dto.request.AddressCreateRequestDto;
import com.nhnacademy.shop.address.dto.request.AddressModifyRequestDto;
import com.nhnacademy.shop.address.dto.response.AddressResponseDto;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.address.service.impl.AddressServiceImpl;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Address Service Test
 *
 * @Author: jinjiwon
 * @Date: 02/04/2024
 */
@ExtendWith(MockitoExtension.class)
@Transactional
@WebAppConfiguration
class AddressServiceTest {
    @Mock
    private final AddressRepository addressRepository = mock(AddressRepository.class);

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private MemberRepository memberRepository;

    Address address;
    Customer customer;
    Grade grade;
    Member member;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .customerNo(1L)
//                .customerId("abcd")
                .customerPassword("1234")
                .customerName("홍길동")
                .customerPhoneNumber("01051745441")
                .customerEmail("jin@naver.com")
                .customerBirthday(LocalDate.of(2001, 2, 21))
                .customerRole("ROLE_MEMBER")
                .build();

        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("silver")
                .accumulateRate(30L)
                .build();

        member = Member.builder()
                .customerNo(1L)
                .customer(customer)
                .memberId("abcd")
                .lastLoginAt(LocalDateTime.of(2024, 4, 4, 10, 42))
                .grade(grade)
                .role("ROLE_MEMBER")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        address = Address.builder()
                .addressId(1L)
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 정보화본부 교육관 1303호")
                .isDefault(false)
                .member(member)
                .build();
    }

    @Test
    @DisplayName(value = "주소 등록")
    void testSaveAddress() {
        AddressCreateRequestDto addressCreateRequestDto = AddressCreateRequestDto.builder()
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 정보화본부 교육관 1303호")
                .isDefault(false)
                .customerNo(member.getCustomerNo())
                .build();

        AddressResponseDto addressResponseDto = AddressResponseDto.builder()
                .addressId(1L)
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 정보화본부 교육관 1303호")
                .isDefault(false)
                .build();

        when(addressRepository.save(any())).thenReturn(address);
        when(memberRepository.findMemberByCustomerNo(any())).thenReturn(member);

        AddressResponseDto result = addressService.saveAddress(addressCreateRequestDto);
        assertThat(result.getAlias()).isEqualTo(addressResponseDto.getAlias());
        assertThat(result.getReceiverName()).isEqualTo(addressResponseDto.getReceiverName());
        assertThat(result.getReceiverPhoneNumber()).isEqualTo(addressResponseDto.getReceiverPhoneNumber());
        assertThat(result.getZipcode()).isEqualTo(addressResponseDto.getZipcode());
        assertThat(result.getAddress()).isEqualTo(addressResponseDto.getAddress());
        assertThat(result.getAddressDetail()).isEqualTo(addressResponseDto.getAddressDetail());
        assertThat(result.getIsDefault()).isEqualTo(addressResponseDto.getIsDefault());
    }

    @Test
    @DisplayName(value = "주소 조회")
    void testGetAddresses() {
        Long customerNo = 1L;

        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        when(addressRepository.findByMemberCustomerNo(customerNo)).thenReturn(addresses);

        List<AddressResponseDto> addressResponseDtos = addressService.getAddresses(customerNo);

        assertThat(addressResponseDtos)
                .isNotNull()
                .hasSize(1);

        AddressResponseDto responseDto = addressResponseDtos.get(0);
        assertThat(responseDto.getAddressId()).isEqualTo(address.getAddressId());
        assertThat(responseDto.getAlias()).isEqualTo(address.getAlias());
        assertThat(responseDto.getReceiverName()).isEqualTo(address.getReceiverName());
        assertThat(responseDto.getReceiverPhoneNumber()).isEqualTo(address.getReceiverPhoneNumber());
        assertThat(responseDto.getZipcode()).isEqualTo(address.getZipcode());
        assertThat(responseDto.getAddress()).isEqualTo(address.getAddress());
        assertThat(responseDto.getAddressDetail()).isEqualTo(address.getAddressDetail());
        assertThat(responseDto.getIsDefault()).isEqualTo(address.getIsDefault());
    }

    @Test
    @DisplayName(value = "주소 수정")
    void testModifyAddress() {
        Long addressId = 1L;
        Long customerNo = 1L;
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        AddressModifyRequestDto addressModifyRequestDto = AddressModifyRequestDto.builder()
                .alias("직장")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051740000")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("충남대학교 기숙사 11동")
                .isDefault(true)
                .build();

        Address updateAddress = Address.builder()
                .addressId(address.getAddressId())
                .alias(addressModifyRequestDto.getAlias())
                .receiverName(addressModifyRequestDto.getReceiverName())
                .receiverPhoneNumber(addressModifyRequestDto.getReceiverPhoneNumber())
                .zipcode(addressModifyRequestDto.getZipcode())
                .address(addressModifyRequestDto.getAddress())
                .addressDetail(addressModifyRequestDto.getAddressDetail())
                .isDefault(addressModifyRequestDto.getIsDefault())
                .member(address.getMember())
                .build();

        when(addressRepository.save(any())).thenReturn(updateAddress);
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(addressRepository.findByMemberCustomerNo(customerNo)).thenReturn(addresses);

        AddressResponseDto result = addressService.modifyAddress(addressId, addressModifyRequestDto);

        assertThat(result.getAlias()).isEqualTo(addressModifyRequestDto.getAlias());
        assertThat(result.getReceiverName()).isEqualTo(addressModifyRequestDto.getReceiverName());
        assertThat(result.getReceiverPhoneNumber()).isEqualTo(addressModifyRequestDto.getReceiverPhoneNumber());
        assertThat(result.getZipcode()).isEqualTo(addressModifyRequestDto.getZipcode());
        assertThat(result.getAddress()).isEqualTo(addressModifyRequestDto.getAddress());
        assertThat(result.getAddressDetail()).isEqualTo(addressModifyRequestDto.getAddressDetail());
        assertThat(result.getIsDefault()).isEqualTo(addressModifyRequestDto.getIsDefault());
    }

    @Test
    @DisplayName(value = "주소 삭제")
    void testDeleteAddress() {
        Long addressId = 1L;
        doNothing().when(addressRepository).deleteById(addressId);
        addressService.deleteAddress(addressId);
        verify(addressRepository, times(1)).deleteById(addressId);
    }
}
