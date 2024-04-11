package com.nhnacademy.shop.book.repository.impl;

import com.nhnacademy.shop.author.domain.QAuthor;
import com.nhnacademy.shop.book.domain.Book;
import com.nhnacademy.shop.book.domain.QBook;
import com.nhnacademy.shop.book.dto.response.BookResponseDto;
import com.nhnacademy.shop.book.exception.BookNotFoundException;
import com.nhnacademy.shop.book.repository.BookRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Objects;


/**
 * 도서관리 repository에서 query dsl을 사용하기 위한 custom repository 구현체
 *
 * @author : 이재원
 * @date : 2024-03-11
 */
public class BookRepositoryCustomImpl extends QuerydslRepositorySupport implements BookRepositoryCustom {

    private final EntityManager entityManager;

    public BookRepositoryCustomImpl(EntityManager entityManager){
        super(Book.class);
        this.entityManager = entityManager;
    }

    @Override
    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        QBook book = QBook.book;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<BookResponseDto> query = queryFactory
                .from(book)
                .orderBy(book.bookIsbn.asc())
                .select(Projections.constructor(BookResponseDto.class,
                        book.bookIsbn, book.bookDesc, book.bookPublisher, book.bookPublisherAt,
                        book.bookIsPacking, book.bookImage, book.bookStatus, book.bookFixedPrice,
                        book.bookQuantity, book.bookSalePrice, book.bookTitle, book.bookViews))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(book.count()).from(book).fetchOne();

        if(Objects.isNull(count)){
            throw new BookNotFoundException();
        }

        return new PageImpl<>(query.fetch(),pageable, count);
    }
    @Override
    public Page<BookResponseDto> getBooksByBookTitle(Pageable pageable, String bookTitle){
        QBook book = QBook.book;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<BookResponseDto> query = queryFactory.from(book)
                .select(Projections.constructor(BookResponseDto.class,
                        book.bookIsbn, book.bookDesc, book.bookPublisher, book.bookPublisherAt,
                        book.bookIsPacking, book.bookImage, book.bookStatus, book.bookFixedPrice,
                        book.bookQuantity, book.bookSalePrice, book.bookTitle, book.bookViews))
                .where(book.bookTitle.like(bookTitle))
                .orderBy(book.bookIsbn.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(book.count())
                .where(book.bookTitle.like(bookTitle))
                .from(book)
                .fetchOne();

        if(Objects.isNull(count)){
            throw new BookNotFoundException();
        }

        return new PageImpl<>(query.fetch(), pageable, count);
    }

    @Override
    public Page<BookResponseDto> getBooksByBookDesc(Pageable pageable, String desc){
        QBook book = QBook.book;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<BookResponseDto> query = queryFactory.from(book)
                .select(Projections.constructor(BookResponseDto.class,
                        book.bookIsbn, book.bookDesc, book.bookPublisher, book.bookPublisherAt,
                        book.bookIsPacking, book.bookImage, book.bookStatus, book.bookFixedPrice,
                        book.bookQuantity, book.bookSalePrice, book.bookTitle, book.bookViews))
                .where(book.bookDesc.like(desc))
                .orderBy(book.bookIsbn.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.select(book.count())
                .where(book.bookDesc.like(desc))
                .from(book)
                .fetchOne();

        if(Objects.isNull(count)){
            throw new BookNotFoundException();
        }


        return new PageImpl<>(query.fetch(), pageable, count);
    }

    @Override
    public Page<BookResponseDto> getBooksByAuthor(Pageable pageable, Long authorId){
        QBook book = QBook.book;
        QAuthor author = QAuthor.author;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        JPAQuery<BookResponseDto> query = queryFactory.from(book)
                .innerJoin(author)
                .on(book.author.authorId.eq(author.authorId))
                .where(book.author.authorId.eq(authorId))
                .select(Projections.constructor(BookResponseDto.class,
                        book.bookIsbn, book.bookDesc, book.bookPublisher, book.bookPublisherAt,
                        book.bookIsPacking, book.bookImage, book.bookStatus, book.bookFixedPrice,
                        book.bookQuantity, book.bookSalePrice, book.bookTitle, book.bookViews))
                .orderBy(book.bookIsbn.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        Long count = queryFactory.from(book)
                .select(book.count())
                .innerJoin(author)
                .on(book.author.authorId.eq(author.authorId))
                .where(book.author.authorId.eq(authorId))
                .fetchOne();

        if(Objects.isNull(count)){
            throw new BookNotFoundException();
        }

        return new PageImpl<>(query.fetch(), pageable, count);
    }

}
