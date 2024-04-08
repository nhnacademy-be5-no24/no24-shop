package com.nhnacademy.shop.point.controller;

import com.nhnacademy.shop.member.exception.MemberNotFoundException;
import com.nhnacademy.shop.point.dto.request.PointRequestDto;
import com.nhnacademy.shop.point.dto.response.PointResponseDto;
import com.nhnacademy.shop.point.service.PointLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 포인트 내역(PointLog) RestController 입니다.
 *
 * @author : 강병구
 * @date : 2024-04-04
 */

@RestController
@RequestMapping(value = "/shop")
public class PointLogController {
    @Autowired
    private final PointLogService pointLogService;

    public PointLogController(PointLogService pointLogService) {
        this.pointLogService = pointLogService;
    }

    /**
     * 포인트 내역 전체 조회 요청 시 사용되는 메소드입니다.
     *
     * @param pageSize 페이지 구성을 위한 페이지 사이즈 입니다.
     * @param offset   페이지 구성을 위한 페이지 오프셋 입니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/points")
    public ResponseEntity<Page<PointResponseDto>> getPoints(@RequestParam Integer pageSize,
                                                            @RequestParam Integer offset) {
        Page<PointResponseDto> dtoList = pointLogService.getPointLogs(pageSize, offset);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoList);
    }

    /**
     * 회원 포인트 내역 전체 조회 요청 시 사용되는 메소드입니다.
     *
     * @param customerNo 조회를 위한 회원 번호 입니다.
     * @param pageSize 페이지 구성을 위한 페이지 사이즈 입니다.
     * @param offset 페이지 구성을 위한 페이지 오프셋 입니다.
     * @throws ResponseStatusException 회원을 찾을 수 없을 때 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/points/{customerNo}")
    public ResponseEntity<Page<PointResponseDto>> getPointsByCustomerNo(@PathVariable Long customerNo,
                                                                        @RequestParam Integer pageSize,
                                                                        @RequestParam Integer offset) {
        try {
            Page<PointResponseDto> dtoList = pointLogService.getPointsByCustomerNo(customerNo, pageSize, offset);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dtoList);
        } catch (MemberNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member Not Found : %d", customerNo));
        }
    }

    /**
     * 회원 포인트 내역 날짜 별 조회 요청 시 사용되는 메소드입니다.
     *
     * @param customerNo 조회를 위한 회원 번호 입니다.
     * @param startDate 시작 일자 입니다.
     * @param endDate 종료 일자 입니다.
     * @param pageSize 페이지 구성을 위한 페이지 사이즈 입니다.
     * @param offset 페이지 구성을 위한 페이지 오프셋 입니다.
     * @throws ResponseStatusException 회원을 찾을 수 없을 때 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping("/points/{customerNo}/date")
    public ResponseEntity<Page<PointResponseDto>> getPointsByCustomerNoAndCreatedAt(@PathVariable Long customerNo,
                                                                        @RequestParam LocalDateTime startDate,
                                                                        @RequestParam LocalDateTime endDate,
                                                                        @RequestParam Integer pageSize,
                                                                        @RequestParam Integer offset) {
        try {
            Page<PointResponseDto> dtoList = pointLogService.getPointsByCustomerNoAndCreatedAt(customerNo, startDate, endDate, pageSize, offset);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dtoList);
        } catch (MemberNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Member Not Found : %d", customerNo));
        }
    }

    /**
     * 회원 포인트 내역 생성 요청 시 사용되는 메소드입니다.
     *
     * @param pointRequestDto 생성할 포인트 내연 정보를 담고 있는 dto 입니다.
     * @return 성공했을 때 응답코드 201 CREATED 반환합니다.
     */
    @PostMapping("/points")
    public ResponseEntity<PointResponseDto> createPoints(@RequestBody @Valid PointRequestDto pointRequestDto) {
        PointResponseDto dto = pointLogService.createPointLog(pointRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

}
