package com.nhnacademy.shop.like.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.like.dto.request.LikeMemberBookReqeustDto;
import com.nhnacademy.shop.member.dto.MemberDto;

/**
 * 도서관리 Service interface
 *
 * @author : 이재원
 * @date : 2024-04-01
 */
@Service
public interface LikeService {

    void createLike(LikeMemberBookReqeustDto request);

    void deleteLike(LikeMemberBookReqeustDto reqeust);

    List<MemberDto> getLikeByIsbn(String bookIsbn);

    List<BookResponseDto> getLikeByMember(Long customerNo);

    
    
}
