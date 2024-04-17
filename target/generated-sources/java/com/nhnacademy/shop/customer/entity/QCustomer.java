package com.nhnacademy.shop.customer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = -957543320L;

    public static final QCustomer customer = new QCustomer("customer");

    public final DatePath<java.time.LocalDate> customerBirthday = createDate("customerBirthday", java.time.LocalDate.class);

    public final StringPath customerEmail = createString("customerEmail");

    public final StringPath customerId = createString("customerId");

    public final StringPath customerName = createString("customerName");

    public final NumberPath<Long> customerNo = createNumber("customerNo", Long.class);

    public final StringPath customerPassword = createString("customerPassword");

    public final StringPath customerPhoneNumber = createString("customerPhoneNumber");

    public final StringPath customerRole = createString("customerRole");

    public QCustomer(String variable) {
        super(Customer.class, forVariable(variable));
    }

    public QCustomer(Path<? extends Customer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCustomer(PathMetadata metadata) {
        super(Customer.class, metadata);
    }

}

