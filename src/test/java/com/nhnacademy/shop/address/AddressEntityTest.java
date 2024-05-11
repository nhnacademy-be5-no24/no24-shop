package com.nhnacademy.shop.address;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
class AddressEntityTest {
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
    }

    @Test
    void testAddressEntityGetterTest() {
        Address sampleAddress = Address.builder()
                .addressId(1L)
                .alias("집")
                .receiverName("홍길동")
                .receiverPhoneNumber("01012345678")
                .zipcode("12345")
                .address("대전광역시 유성구 대학로99")
                .addressDetail("CNU빌라")
                .isDefault(false)
                .member(member)
                .build();

        Long addressId = sampleAddress.getAddressId();
        String alias = sampleAddress.getAlias();
        String receiverName = sampleAddress.getReceiverName();
        String receiverPhoneNumber = sampleAddress.getReceiverPhoneNumber();
        String zipcode = sampleAddress.getZipcode();
        String address = sampleAddress.getAddress();
        String addressDetail = sampleAddress.getAddressDetail();
        Boolean isDefault = sampleAddress.getIsDefault();
        Long customerNo = sampleAddress.getMember().getCustomerNo();

        assertEquals(Long.valueOf(1L), addressId);
        assertEquals("집", alias);
        assertEquals("홍길동", receiverName);
        assertEquals("01012345678", receiverPhoneNumber);
        assertEquals("12345", zipcode);
        assertEquals("대전광역시 유성구 대학로99", address);
        assertEquals("CNU빌라", addressDetail);
        assertEquals(false, isDefault);
        assertEquals(Long.valueOf(1L), customerNo);
    }
}