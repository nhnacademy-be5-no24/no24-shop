package com.nhnacademy.shop.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAmountCoupon is a Querydsl query type for AmountCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAmountCoupon extends EntityPathBase<AmountCoupon> {

    private static final long serialVersionUID = 1232279088L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAmountCoupon amountCoupon = new QAmountCoupon("amountCoupon");

    public final QCoupon coupon;

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    public final NumberPath<Long> discountPrice = createNumber("discountPrice", Long.class);

    public QAmountCoupon(String variable) {
        this(AmountCoupon.class, forVariable(variable), INITS);
    }

    public QAmountCoupon(Path<? extends AmountCoupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAmountCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAmountCoupon(PathMetadata metadata, PathInits inits) {
        this(AmountCoupon.class, metadata, inits);
    }

    public QAmountCoupon(Class<? extends AmountCoupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon")) : null;
    }

}

