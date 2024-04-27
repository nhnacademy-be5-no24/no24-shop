package com.nhnacademy.shop.point.repository;

import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.domain.Grade;
import com.nhnacademy.shop.grade.repository.GradeRespository;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Point Repository 테스트 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-05
 */

@DataJpaTest
@ActiveProfiles(value = "dev")
@WebAppConfiguration
public class PointRepositoryTest {
    @Autowired
    private PointLogRepository pointLogRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GradeRespository gradeRespository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EntityManager entityManager;
    private Customer customer;
    private Grade grade;
    private Member member;
    private PointLog pointLog;

    @BeforeEach
    void setUp() {
        grade = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .accumulateRate(5L)
                .build();

        grade = gradeRespository.save(grade);

        String orderId = "example_order";

        customer = Customer.builder()
                .customerNo(1L)
                .customerId("123")
                .customerPassword("password")
                .customerName("name")
                .customerPhoneNumber("010-1234-2345")
                .customerEmail("name@naver.com")
                .customerBirthday(LocalDate.of(2000, 11, 30))
                .customerRole("GUEST")
                .build();

        customer = customerRepository.save(customer);

        member = Member.builder()
                .memberId("123")
                .customer(customer)
                .lastLoginAt(LocalDateTime.now())
                .grade(grade)
                .role("ROLE_ADMIN")
                .memberState(Member.MemberState.ACTIVE)
                .build();

        pointLog = PointLog.builder()
                .pointId(1L)
                .member(member)
                .orderId(orderId)
                .pointDescription("리뷰 작성")
                .pointUsage(500)
                .createdAt(LocalDateTime.parse("2024-04-05T00:00:00")).build();
    }

    @Test
    @Order(1)
    @DisplayName(value = "전체 포인트 내역 조회")
    void testFindPoints() {
        memberRepository.save(member);

        pointLogRepository.save(pointLog);

        Pageable pageable = PageRequest.of(0, 10);
        Page<PointResponseDto> dtoPage = pointLogRepository.findPoints(pageable);
        List<PointResponseDto> pointList = dtoPage.getContent();

        assertThat(pointList).isNotEmpty();
        assertThat(pointList.get(0).getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(pointList.get(0).getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(pointList.get(0).getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(pointList.get(0).getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(pointList.get(0).getUsage()).isEqualTo(pointLog.getPointUsage());
        assertThat(pointList.get(0).getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }

    @Test
    @Order(2)
    @DisplayName(value = "회원 전체 포인트 내역 조회 (회원 번호)")
    void testFindPointsByCustomerNo() {
        memberRepository.save(member);

        pointLogRepository.save(pointLog);

        Pageable pageable = PageRequest.of(0, 10);
        Page<PointResponseDto> dtoPage = pointLogRepository.findPointsByCustomerNo(pointLog.getMember().getCustomerNo(), pageable);
        List<PointResponseDto> pointList = dtoPage.getContent();

        assertThat(pointList).isNotEmpty();
        assertThat(pointList.get(0).getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(pointList.get(0).getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(pointList.get(0).getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(pointList.get(0).getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(pointList.get(0).getUsage()).isEqualTo(pointLog.getPointUsage());
        assertThat(pointList.get(0).getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }

    @Test
    @Order(3)
    @DisplayName(value = "회원 날짜 별 포인트 내역 조회 (회원 번호, 시작 일자, 종료 일자)")
    void testFindPointsByCustomerNoAndCreatedAt() {
        memberRepository.save(member);

        pointLogRepository.save(pointLog);

        LocalDateTime startDate = LocalDateTime.parse("2024-04-03T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2024-04-06T00:00:00");

        Pageable pageable = PageRequest.of(0, 10);
        Page<PointResponseDto> dtoPage = pointLogRepository.findPointsByCustomerNoAndCreatedAt(pointLog.getMember().getCustomerNo(), startDate, endDate, pageable);
        List<PointResponseDto> pointList = dtoPage.getContent();

        assertThat(pointList).isNotEmpty();
        assertThat(pointList.get(0).getPointId()).isEqualTo(pointLog.getPointId());
        assertThat(pointList.get(0).getCustomerNo()).isEqualTo(pointLog.getMember().getCustomerNo());
        assertThat(pointList.get(0).getOrderId()).isEqualTo(pointLog.getOrderId());
        assertThat(pointList.get(0).getPointDescription()).isEqualTo(pointLog.getPointDescription());
        assertThat(pointList.get(0).getUsage()).isEqualTo(pointLog.getPointUsage());
        assertThat(pointList.get(0).getCreatedAt()).isEqualTo(pointLog.getCreatedAt());
    }


    @AfterEach
    public void teardown() {
        pointLogRepository.deleteAll();
        memberRepository.deleteAll();

        this.entityManager
                .createNativeQuery("ALTER TABLE point_log ALTER COLUMN `point_id` RESTART                                                                     WITH 1")
                .executeUpdate();
    }

}
