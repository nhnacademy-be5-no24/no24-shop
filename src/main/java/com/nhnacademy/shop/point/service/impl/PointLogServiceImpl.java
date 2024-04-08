package com.nhnacademy.shop.point.service.impl;

import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.member.repository.MemberRepository;
import com.nhnacademy.shop.point.domain.PointLog;
import com.nhnacademy.shop.point.dto.request.PointRequestDto;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import com.nhnacademy.shop.point.repository.PointLogRepository;
import com.nhnacademy.shop.point.service.PointLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 포인트 내역 서비스 구현체 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */

@Service
public class PointLogServiceImpl implements PointLogService {
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;

    public PointLogServiceImpl(PointLogRepository pointLogRepository, MemberRepository memberRepository) {
        this.pointLogRepository = pointLogRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointResponseDto> getPointLogs(Integer pageSize, Integer offset) {
        return pointLogRepository.findPoints(PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointResponseDto> getPointsByCustomerNo(Long customerNo, Integer pageSize, Integer offset) {
        if (!memberRepository.existsById(customerNo)) {
            throw new MemberNotFoundException();
        }
        return pointLogRepository.findPointsByCustomerNo(customerNo, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PointResponseDto> getPointsByCustomerNoAndCreatedAt(Long customerNo, LocalDateTime startDate, LocalDateTime endDate, Integer pageSize, Integer offset) {
        if (!memberRepository.existsById(customerNo)) {
            throw new MemberNotFoundException();
        }
        return pointLogRepository.findPointsByCustomerNoAndCreatedAt(customerNo, startDate, endDate, PageRequest.of(pageSize, offset));
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 멤버가 존재하지 않을 때 발생하는 exception 입니다.
     */
    @Override
    @Transactional
    public PointResponseDto createPointLog(PointRequestDto pointRequestDto) {
        Member member = memberRepository.findById(pointRequestDto.getCustomerNo()).orElseThrow(MemberNotFoundException::new);

        PointLog pointLog = PointLog.builder()
                .member(member)
                .orderId(pointRequestDto.getOrderId())
                .pointDescription(pointRequestDto.getPointDescription())
                .usage(pointRequestDto.getUsage())
                .isUsed(pointRequestDto.getIsUsed())
                .createdAt(pointRequestDto.getCreatedAt())
                .build();
        PointLog savedPointLog = pointLogRepository.save(pointLog);

        return PointResponseDto.builder()
                .pointId(savedPointLog.getPointId())
                .customerNo(savedPointLog.getMember().getCustomerNo())
                .orderId(savedPointLog.getOrderId())
                .pointDescription(savedPointLog.getPointDescription())
                .usage(savedPointLog.getUsage())
                .isUsed(savedPointLog.getIsUsed())
                .createdAt(savedPointLog.getCreatedAt())
                .build();
    }
}
