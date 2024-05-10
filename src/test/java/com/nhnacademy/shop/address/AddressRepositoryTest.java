package com.nhnacademy.shop.address;

import com.nhnacademy.shop.address.domain.Address;
import com.nhnacademy.shop.address.repository.AddressRepository;
import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.repository.GradeRepository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Address Repository Test
 *
 * @Author: jinjiwon
 * @Date: 01/04/2024
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class AddressRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GradeRepository gradeRepository;

    private Customer customer;
    private Member member;
    private Grade grade;

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
    @DisplayName(value = "멤버 주소록 조회")
    void testFindByMemberCustomerNo() {

        customerRepository.save(customer);
        gradeRepository.save(grade);
        memberRepository.save(member);

        Address address1 = Address.builder()
                .alias("학교")
                .receiverName("홍길동")
                .receiverPhoneNumber("01051745441")
                .zipcode("1234")
                .address("대전광역시 대학로99 충남대학교")
                .addressDetail("정보화본부 1303호")
                .isDefault(false)
                .member(member)
                .build();

        addressRepository.save(address1);

        Address address2 = Address.builder()
                .receiverName("홍길동")
                .receiverPhoneNumber("01051745441")
                .zipcode("1234")
                .address("대전광역시 대학로99 충남대학교")
                .addressDetail("정보화본부 1304호")
                .isDefault(false)
                .member(member)
                .build();

        addressRepository.save(address2);

        assertThat(addressRepository.findByMemberCustomerNo(1L)).hasSize(2);
    }
}
