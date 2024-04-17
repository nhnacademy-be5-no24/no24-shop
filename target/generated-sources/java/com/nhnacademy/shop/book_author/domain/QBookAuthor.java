package com.nhnacademy.shop.book_author.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookAuthor is a Querydsl query type for BookAuthor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookAuthor extends EntityPathBase<BookAuthor> {

    private static final long serialVersionUID = -821464862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookAuthor bookAuthor = new QBookAuthor("bookAuthor");

    public final com.nhnacademy.shop.author.domain.QAuthor author;

    public final com.nhnacademy.shop.book.entity.QBook book;

    public final QBookAuthor_Pk pk;

    public QBookAuthor(String variable) {
        this(BookAuthor.class, forVariable(variable), INITS);
    }

    public QBookAuthor(Path<? extends BookAuthor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookAuthor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookAuthor(PathMetadata metadata, PathInits inits) {
        this(BookAuthor.class, metadata, inits);
    }

    public QBookAuthor(Class<? extends BookAuthor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.nhnacademy.shop.author.domain.QAuthor(forProperty("author")) : null;
        this.book = inits.isInitialized("book") ? new com.nhnacademy.shop.book.entity.QBook(forProperty("book")) : null;
        this.pk = inits.isInitialized("pk") ? new QBookAuthor_Pk(forProperty("pk")) : null;
    }

}

