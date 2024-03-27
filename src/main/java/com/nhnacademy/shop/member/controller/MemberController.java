//package com.nhnacademy.shop.member.controller;
//
//import com.nhnacademy.shop.member.adapter.MemberAdapter;
//import com.nhnacademy.shop.member.dto.MemberDto;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Objects;
//
//@RestController
//@RequestMapping("/member")
//public class MemberController {
//    private final MemberAdapter memberAdapter;
//
//    public MemberController(MemberAdapter memberAdapter) {
//        this.memberAdapter = memberAdapter;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
//        MemberDto memberDto = memberAdapter.getMember(id);
//        if(Objects.isNull(memberDto)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member Not Found : " + id);
//        }
//        return ResponseEntity.ok(memberDto);
//    }
//}
