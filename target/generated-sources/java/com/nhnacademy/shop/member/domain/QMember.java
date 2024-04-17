package com.nhnacademy.shop.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1994745057L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.nhnacademy.shop.customer.entity.QCustomer customer;

    public final NumberPath<Long> customerNo = createNumber("customerNo", Long.class);

    public final com.nhnacademy.shop.grade.domain.QGrade grade;

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final StringPath memberId = createString("memberId");

    public final EnumPath<Member.MemberState> memberState = createEnum("memberState", Member.MemberState.class);

    public final StringPath role = createString("role");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.customer = inits.isInitialized("customer") ? new com.nhnacademy.shop.customer.entity.QCustomer(forProperty("customer")) : null;
        this.grade = inits.isInitialized("grade") ? new com.nhnacademy.shop.grade.domain.QGrade(forProperty("grade")) : null;
    }

}

