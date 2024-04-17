package com.nhnacademy.shop.address.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddress extends EntityPathBase<Address> {

    private static final long serialVersionUID = 1176836723L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAddress address1 = new QAddress("address1");

    public final StringPath address = createString("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final NumberPath<Long> addressId = createNumber("addressId", Long.class);

    public final StringPath alias = createString("alias");

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final com.nhnacademy.shop.member.domain.QMember member;

    public final StringPath receiverName = createString("receiverName");

    public final StringPath receiverPhoneNumber = createString("receiverPhoneNumber");

    public final StringPath zipcode = createString("zipcode");

    public QAddress(String variable) {
        this(Address.class, forVariable(variable), INITS);
    }

    public QAddress(Path<? extends Address> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAddress(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAddress(PathMetadata metadata, PathInits inits) {
        this(Address.class, metadata, inits);
    }

    public QAddress(Class<? extends Address> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.nhnacademy.shop.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

