package com.nhnacademy.shop.point.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPointLog is a Querydsl query type for PointLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointLog extends EntityPathBase<PointLog> {

    private static final long serialVersionUID = 1074568913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPointLog pointLog = new QPointLog("pointLog");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.nhnacademy.shop.member.domain.QMember member;

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final StringPath pointDescription = createString("pointDescription");

    public final NumberPath<Long> pointId = createNumber("pointId", Long.class);

    public final BooleanPath pointType = createBoolean("pointType");

    public final NumberPath<Integer> pointUsage = createNumber("pointUsage", Integer.class);

    public QPointLog(String variable) {
        this(PointLog.class, forVariable(variable), INITS);
    }

    public QPointLog(Path<? extends PointLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPointLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPointLog(PathMetadata metadata, PathInits inits) {
        this(PointLog.class, metadata, inits);
    }

    public QPointLog(Class<? extends PointLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.nhnacademy.shop.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

