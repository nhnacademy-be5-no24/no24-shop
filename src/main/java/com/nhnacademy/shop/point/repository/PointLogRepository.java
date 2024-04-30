package com.nhnacademy.shop.point.repository;

import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.point.domain.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 포인트 내역(PointLog) 레포지토리 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */
public interface PointLogRepository extends JpaRepository<PointLog, Long>, PointLogRepositoryCustom {
    List<PointLog> findByMember(Member member);
}
