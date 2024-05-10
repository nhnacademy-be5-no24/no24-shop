package com.nhnacademy.shop.grade.controller;

import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.grade.dto.request.GradeRequestDto;
import com.nhnacademy.shop.grade.dto.response.GradeResponseDto;
import com.nhnacademy.shop.grade.dto.response.GradeResponseDtoList;
import com.nhnacademy.shop.grade.service.GradeService;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 설명
 *
 * @Author : 박병휘
 * @Date : 2024/05/03
 */
@RestController
@RequestMapping("/shop/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    private final MemberRepository memberRepository;

    @GetMapping("/all")
    public ResponseEntity<GradeResponseDtoList> getAllGrades() {
        return ResponseEntity.ok().body(new GradeResponseDtoList(gradeService.findAllGrades()));
    }

    @GetMapping("/id/{gradeId}")
    public ResponseEntity<GradeResponseDto> getGradeById(@PathVariable Long gradeId) {
        try {
            return ResponseEntity.ok().body(gradeService.findGradeById(gradeId));
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerNo}")
    public ResponseEntity<GradeResponseDto> getGradeByCustomerNo(@PathVariable Long customerNo) {
        Optional<Member> optionalMember = memberRepository.findById(customerNo);

        if(optionalMember.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(new GradeResponseDto(optionalMember.get().getGrade()));
    }

    @PostMapping("/create")
    public ResponseEntity<GradeResponseDto> createGrade(@RequestBody GradeRequestDto gradeRequestDto) {
        try {
            return ResponseEntity.ok().body(gradeService.saveGrade(gradeRequestDto));
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<GradeResponseDto> updateGrade(@RequestBody GradeRequestDto gradeRequestDto) {
        try {
            return ResponseEntity.ok().body(gradeService.updateGrade(gradeRequestDto));
        } catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
