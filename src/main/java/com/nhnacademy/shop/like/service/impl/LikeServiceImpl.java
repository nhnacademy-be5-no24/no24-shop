package com.nhnacademy.shop.like.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.nhnacademy.auth.user.domain.Member;
import com.nhnacademy.auth.user.dto.MemberCreateDto;
import com.nhnacademy.auth.user.repository.MemberRepository;
import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.repository.BookRepository;
import com.nhnacademy.shop.like.domain.Like;
import com.nhnacademy.shop.like.dto.request.LikeMemberBookReqeustDto;
import com.nhnacademy.shop.like.repository.LikeRepository;
import com.nhnacademy.shop.like.service.LikeService;

import lombok.RequiredArgsConstructor;

/**
 * 좋아요 Service 
 *
 * @author : 이재원
 * @date : 2024-04-02
 */
@Service("likeService")
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    
    /*
     * Like 생성
     * @Param : LikeMemberBookRequestDto request
     */
    @Override
    @Transactional
    public void createLike(LikeMemberBookReqeustDto request) {

        Book book = bookRepository.findByBookIsbn(request.getBookIsbn());
        Member member = memberRepository.findByCustomerNo(request.getCustomerNo());

        Like like = Like.builder().book(book).member(member).build();
        likeRepository.save(like);

    }

    /*
     * Like 삭제
     * @Param : LikeMemberBookRequestDto request
     */
    @Override
    @Transactional
    public void deleteLike(LikeMemberBookReqeustDto reqeust) {
        Book book = bookRepository.findByBookIsbn(reqeust.getBookIsbn());
        Member member = memberRepository.findByCustomerNo(reqeust.getCustomerNo());

        likeRepository.deleteByBookAndMember(book, member);
    }

    /*
     * Like 탐색 - book_isbn
     * @Param : String bookIsbn
     */
    @Override
    @Transactional
    public List<MemberCreateDto> getLikeByIsbn(String bookIsbn) {
        Book book = bookRepository.findByBookIsbn(bookIsbn);
        List<MemberCreateDto> response = new ArrayList<>();
        List<Like> likes = likeRepository.findByBook(book);

        for(Like like : likes){
            Member member = like.getMember();
            response.add(new MemberCreateDto(member.getMemberId(), member.getCustomer().getCustomerPassword(), member.getCustomer().getCustomerName(), member.getCustomer().getCustomerPhoneNumber(),
            member.getCustomer().getCustomerEmail(), member.getCustomer().getCustomerBirthday(), member.getGrade().getGradeId()));
        }

        return response;
    }

    /*
     * Like 탐색 - customer_no
     * @Param : Long customerNo
     */
    @Override
    @Transactional
    public List<BookResponseDto> getLikeByMember(Long customerNo) {
        Member member = memberRepository.findByCustomerNo(customerNo);
        List<BookResponseDto> respose = new ArrayList<>();
        List<Like> likes = likeRepository.findByMember(member);

        for(Like like : likes){
            Book book = like.getBook();
            respose.add(new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher() ,book.getBookPublisherAt(),
            book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
            book.getBookImage(), book.getTags(), book.getAuthors(), book.getCategories(), null));
        }

        return respose;
    }
    

}
