package com.nhnacademy.shop.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookCoupon is a Querydsl query type for BookCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookCoupon extends EntityPathBase<BookCoupon> {

    private static final long serialVersionUID = 47107201L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookCoupon bookCoupon = new QBookCoupon("bookCoupon");

    public final StringPath bookIsbn = createString("bookIsbn");

    public final QCoupon coupon;

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    public QBookCoupon(String variable) {
        this(BookCoupon.class, forVariable(variable), INITS);
    }

    public QBookCoupon(Path<? extends BookCoupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookCoupon(PathMetadata metadata, PathInits inits) {
        this(BookCoupon.class, metadata, inits);
    }

    public QBookCoupon(Class<? extends BookCoupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
    }

}

