package com.nhnacademy.shop.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = -603320008L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    public final StringPath couponName = createString("couponName");

    public final EnumPath<Coupon.Status> couponStatus = createEnum("couponStatus", Coupon.Status.class);

    public final EnumPath<Coupon.CouponTarget> couponTarget = createEnum("couponTarget", Coupon.CouponTarget.class);

    public final EnumPath<Coupon.CouponType> couponType = createEnum("couponType", Coupon.CouponType.class);

    public final DateTimePath<java.util.Date> deadline = createDateTime("deadline", java.util.Date.class);

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

