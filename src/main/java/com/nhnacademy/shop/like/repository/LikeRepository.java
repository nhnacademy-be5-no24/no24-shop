package com.nhnacademy.shop.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.shop.like.domain.Like;
import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.member.domain.Member;
import java.util.List;



/**
 * 좋아요 repository
 *
 * @author : 이재원
 * @date : 2024-04-01
 */
public interface LikeRepository extends JpaRepository<Like, Long>{

    void deleteByBookAndMember(Book book, Member member);

    List<Like> findByBook(Book book);

    List<Like> findByMember(Member member);
    
}
