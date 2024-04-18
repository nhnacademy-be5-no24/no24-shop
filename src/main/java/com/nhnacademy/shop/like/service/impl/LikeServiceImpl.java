package com.nhnacademy.shop.like.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.dto.CustomerDto;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import com.nhnacademy.shop.member.dto.MemberDto;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.member.domain.Member;
import com.nhnacademy.shop.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import com.nhnacademy.shop.book.entity.Book;
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
    private final CustomerRepository customerRepository;
    /*
     * Like 생성
     * @Param : LikeMemberBookRequestDto request
     */
    @Override
    @Transactional
    public void createLike(LikeMemberBookReqeustDto request) {

        Book book = bookRepository.findByBookIsbn(request.getBookIsbn()).orElseThrow(BookNotFoundException::new);
        Member member = memberRepository.findMemberByCustomerNo(request.getCustomerNo());

        Like like = Like.builder().book(book).member(member).build();
        likeRepository.save(like);

        bookRepository.save(new Book(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews()
                , book.getBookStatus(), book.getBookQuantity(), book.getBookImage(), book.getCategories(), book.getTags(), book.getAuthor(), book.getLikes()+1L));

    }

    /*
     * Like 삭제
     * @Param : LikeMemberBookRequestDto request
     */
    @Override
    @Transactional
    public void deleteLike(LikeMemberBookReqeustDto request) {
        Book book = bookRepository.findByBookIsbn(request.getBookIsbn()).orElseThrow(BookNotFoundException::new);
        Member member = memberRepository.findMemberByCustomerNo(request.getCustomerNo());

        likeRepository.deleteByBookAndMember(book, member);

        bookRepository.save(new Book(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(), book.getBookPublisher(),
                book.getBookPublishedAt(), book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews()
                , book.getBookStatus(), book.getBookQuantity(), book.getBookImage(), book.getCategories(), book.getTags(), book.getAuthor(), book.getLikes()-1L));
    }

    /*
     * Like 탐색 - book_isbn
     * @Param : String bookIsbn
     */
    @Override
    @Transactional
    public List<MemberDto> getLikeByIsbn(String bookIsbn) {
        Book book = bookRepository.findByBookIsbn(bookIsbn).orElseThrow(BookNotFoundException::new);
        List<MemberDto> response = new ArrayList<>();
        List<Like> likes = likeRepository.findByBook(book);

        for(Like like : likes){
            Member member = like.getMember();
            Customer customer = customerRepository.findById(member.getCustomerNo()).orElseThrow();
            CustomerDto customerDto = new CustomerDto(customer.getCustomerId(), customer.getCustomerPassword(), customer.getCustomerName(), customer.getCustomerPhoneNumber(),
                    customer.getCustomerEmail(), customer.getCustomerBirthday(), customer.getCustomerRole());

            response.add(new MemberDto(customerDto, member.getMemberId(), member.getLastLoginAt(), member.getGrade().getGradeId(),
                    member.getMemberState(), member.getRole()));
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
        Member member = memberRepository.findMemberByCustomerNo(customerNo);
        List<BookResponseDto> response = new ArrayList<>();
        List<Like> likes = likeRepository.findByMember(member);

        for(Like like : likes){
            Book book = like.getBook();
            response.add(new BookResponseDto(book.getBookIsbn(), book.getBookTitle(), book.getBookDesc(),book.getBookPublisher() ,book.getBookPublishedAt(),
            book.getBookFixedPrice(), book.getBookSalePrice(), book.isBookIsPacking(), book.getBookViews(), book.getBookStatus(), book.getBookQuantity(),
            book.getBookImage(), book.getTags(), book.getAuthor(), book.getCategories(), null));
        }

        return response;
    }
    

}
