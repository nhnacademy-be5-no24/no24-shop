package com.nhnacademy.shop.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategoryCoupon is a Querydsl query type for CategoryCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryCoupon extends EntityPathBase<CategoryCoupon> {

    private static final long serialVersionUID = 1634357718L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCategoryCoupon categoryCoupon = new QCategoryCoupon("categoryCoupon");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final QCoupon coupon;

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    public QCategoryCoupon(String variable) {
        this(CategoryCoupon.class, forVariable(variable), INITS);
    }

    public QCategoryCoupon(Path<? extends CategoryCoupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCategoryCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCategoryCoupon(PathMetadata metadata, PathInits inits) {
        this(CategoryCoupon.class, metadata, inits);
    }

    public QCategoryCoupon(Class<? extends CategoryCoupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
    }

}

