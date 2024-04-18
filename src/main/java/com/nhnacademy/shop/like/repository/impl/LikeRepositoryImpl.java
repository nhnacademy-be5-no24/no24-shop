package com.nhnacademy.shop.like.repository.impl;

import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.like.domain.Like;
import com.nhnacademy.shop.like.domain.QLike;
import com.nhnacademy.shop.like.repository.LikeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Objects;

public class LikeRepositoryImpl extends QuerydslRepositorySupport implements LikeRepositoryCustom {

    private final EntityManager entityManager;

    public LikeRepositoryImpl(EntityManager entityManager){
        super(Like.class);
        this.entityManager = entityManager;
    }

    @Override
    public Long countBookLikes(String bookIsbn) {
        QLike like = QLike.like;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        Long count = queryFactory.select(like.count()).where(like.book.bookIsbn.like(bookIsbn)).from(like).fetchOne();

        if(Objects.isNull(count)){
            throw new BookNotFoundException();
        }

        return count;
    }
}
