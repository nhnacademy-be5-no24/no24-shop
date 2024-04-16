package com.nhnacademy.shop.point.repository.impl;

import com.nhnacademy.shop.member.domain.QMember;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.domain.QPointLog;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import com.nhnacademy.shop.point.repository.PointLogRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 포인트 내역 레포지토리 구현체 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */
public class PointLogRepositoryImpl extends QuerydslRepositorySupport implements PointLogRepositoryCustom {
    public PointLogRepositoryImpl() {
        super(PointLog.class);
    }

    QPointLog pointLog = QPointLog.pointLog;
    QMember member = QMember.member;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointResponseDto> findPoints(Pageable pageable) {
        JPQLQuery<Long> count = from(pointLog)
                .select(pointLog.count());

        List<PointResponseDto> content = from(pointLog)
                .select(Projections.constructor(PointResponseDto.class,
                        pointLog.pointId,
                        pointLog.member.customerNo,
                        pointLog.orderId,
                        pointLog.pointDescription,
                        pointLog.usage,
                        pointLog.type,
                        pointLog.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointResponseDto> findPointsByCustomerNo(Long customerNo, Pageable pageable) {
        JPQLQuery<Long> count = from(pointLog)
                .innerJoin(member)
                .on(member.eq(pointLog.member))
                .where(pointLog.member.customerNo.eq(customerNo))
                .select(pointLog.count());

        List<PointResponseDto> content = from(pointLog)
                .innerJoin(member)
                .on(member.eq(pointLog.member))
                .where(pointLog.member.customerNo.eq(customerNo))
                .select(Projections.constructor(PointResponseDto.class,
                        pointLog.pointId,
                        pointLog.member.customerNo,
                        pointLog.orderId,
                        pointLog.pointDescription,
                        pointLog.usage,
                        pointLog.type,
                        pointLog.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PointResponseDto> findPointsByCustomerNoAndCreatedAt(Long customerNo, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        JPQLQuery<Long> count = from(pointLog)
                .innerJoin(member)
                .on(member.eq(pointLog.member))
                .where(pointLog.member.customerNo.eq(customerNo)
                        .and(pointLog.createdAt.between(startDate, endDate)))
                .select(pointLog.count());

        List<PointResponseDto> content = from(pointLog)
                .innerJoin(member)
                .on(member.eq(pointLog.member))
                .where(pointLog.member.customerNo.eq(customerNo)
                        .and(pointLog.createdAt.between(startDate, endDate)))
                .select(Projections.constructor(PointResponseDto.class,
                        pointLog.pointId,
                        pointLog.member.customerNo,
                        pointLog.orderId,
                        pointLog.pointDescription,
                        pointLog.usage,
                        pointLog.type,
                        pointLog.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }
}
