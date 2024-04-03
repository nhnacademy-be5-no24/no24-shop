package com.nhnacademy.shop.like.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nhnacademy.auth.user.dto.MemberCreateDto;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.like.dto.request.LikeMemberBookReqeustDto;
import com.nhnacademy.shop.like.service.LikeService;

import lombok.RequiredArgsConstructor;


/**
 * 좋아요 Controller
 *
 * @author : 이재원
 * @date : 2024-04-03
 */
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;


     /**
     *  새로운 like을 생성하는 method
     *
     * @param LikeMemberBookRequestDto request : 새로운 like을 생성하기 위한 DTO 
     * @return 성공했을 때 응답코드 201 CREATED return
     */
    @PostMapping("/like")
    public ResponseEntity<Void> createLike(LikeMemberBookReqeustDto request){
        likeService.createLike(request);
      
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * LikeMemberBookRequest을 통해서 like을 삭제하는 method
     *
     * @param LikeMemberBookRequestDto reqeust : like 삭제를 위한 정보.
     * @return 성공했을 때 응답코드 204 NO_CONTENT 반환합니다.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteLike(LikeMemberBookReqeustDto reqeust){
        likeService.deleteLike(reqeust);

        return ResponseEntity.noContent().build();
    }

     /**
     * book ISBN을 통해서 좋아요를 누른 member List를 가져오는 method
     *
     * @param String bookIsbn : 해당 book의 Like에 대한 정보를 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<MemberCreateDto>> getLikeByIsbn(String bookIsbn){
        List<MemberCreateDto> memberCreateDtos = likeService.getLikeByIsbn(bookIsbn);
        if(Objects.isNull(memberCreateDtos)){
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Like NOT FOUND");
        }

        return ResponseEntity.status(HttpStatus.OK).body(memberCreateDtos);
    }

    /**
     * customerNo을 통해서 좋아요를 누른 Book List를 가져오는 method
     *
     * @param Long customerNo : 해당 book의 Like에 대한 정보를 찾기 위한 정보.
     * @throws ResponseStatusException book ISBN으로 조회했을 때 찾을 수 없을 경우 응답코드 404 NOT_FOUND 반환합니다.
     * @return 성공했을 때 응답코드 200 OK 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getLikeByMember(Long customerNo){
        List<BookResponseDto> bookResponseDtos = likeService.getLikeByMember(customerNo);
        if(Objects.isNull(bookResponseDtos)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GET : Any Like NOT FOUND");
       }

        return ResponseEntity.status(HttpStatus.OK).body(bookResponseDtos);
    }
    
}
