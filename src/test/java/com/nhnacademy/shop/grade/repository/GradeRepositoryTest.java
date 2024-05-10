package com.nhnacademy.shop.grade.repository;

import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.grade.domain.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 등급 Repository Test
 *
 * @Author : 박병휘
 * @Date : 2024/05/10
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class GradeRepositoryTest {
    private Grade grade1, grade2;

    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    void setUp() {
        grade1 = Grade.builder()
                .gradeId(1L)
                .gradeName("NORMAL")
                .gradeNameKor("일반")
                .accumulateRate(1L)
                .tenPercentCoupon(0)
                .twentyPercentCoupon(0)
                .requiredPayment(0L)
                .build();

        gradeRepository.save(grade1);

        grade2 = Grade.builder()
                .gradeId(2L)
                .gradeName("ROYAL")
                .gradeNameKor("로얄")
                .accumulateRate(2L)
                .tenPercentCoupon(1)
                .twentyPercentCoupon(0)
                .requiredPayment(100000L)
                .build();

        gradeRepository.save(grade2);
    }

    @Test
    @DisplayName(value="등급 리스트 전체 조회")
    void findAllGradesTest() {
        List<Grade> gradeList = gradeRepository.findAll();

        assertEquals(gradeList.size(), 2);
    }

    @Test
    @DisplayName(value="ID 등급 조회")
    void findGradeByGradeId() {
        Optional<Grade> testGrade = gradeRepository.findById(1L);

        assertTrue(testGrade.isPresent());
        assertEquals(testGrade.get(), grade1);
    }

    @Test
    @DisplayName(value="없는 ID 등급 조회")
    void findGradeByDoesNotExistGradeId() {
        Optional<Grade> testGrade = gradeRepository.findById(3L);

        assertTrue(testGrade.isEmpty());
    }
}