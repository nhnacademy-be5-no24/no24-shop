package com.nhnacademy.shop.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPercentageCoupon is a Querydsl query type for PercentageCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPercentageCoupon extends EntityPathBase<PercentageCoupon> {

    private static final long serialVersionUID = 2120246770L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPercentageCoupon percentageCoupon = new QPercentageCoupon("percentageCoupon");

    public final QCoupon coupon;

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    public final NumberPath<Long> discountRate = createNumber("discountRate", Long.class);

    public final NumberPath<Long> maxDiscountPrice = createNumber("maxDiscountPrice", Long.class);

    public QPercentageCoupon(String variable) {
        this(PercentageCoupon.class, forVariable(variable), INITS);
    }

    public QPercentageCoupon(Path<? extends PercentageCoupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPercentageCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPercentageCoupon(PathMetadata metadata, PathInits inits) {
        this(PercentageCoupon.class, metadata, inits);
    }

    public QPercentageCoupon(Class<? extends PercentageCoupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
    }

}

