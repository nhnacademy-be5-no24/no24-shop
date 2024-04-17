package com.nhnacademy.shop.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -153137125L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.nhnacademy.shop.book.entity.QBook book;

    public final com.nhnacademy.shop.member.domain.QMember member;

    public final StringPath reviewContent = createString("reviewContent");

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final StringPath reviewImage = createString("reviewImage");

    public final NumberPath<Integer> reviewScore = createNumber("reviewScore", Integer.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.nhnacademy.shop.book.entity.QBook(forProperty("book")) : null;
        this.member = inits.isInitialized("member") ? new com.nhnacademy.shop.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

