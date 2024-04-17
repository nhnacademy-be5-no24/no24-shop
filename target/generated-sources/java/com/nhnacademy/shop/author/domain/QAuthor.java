package com.nhnacademy.shop.author.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthor is a Querydsl query type for Author
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthor extends EntityPathBase<Author> {

    private static final long serialVersionUID = -2009051327L;

    public static final QAuthor author = new QAuthor("author");

    public final NumberPath<Long> authorId = createNumber("authorId", Long.class);

    public final StringPath authorName = createString("authorName");

    public QAuthor(String variable) {
        super(Author.class, forVariable(variable));
    }

    public QAuthor(Path<? extends Author> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthor(PathMetadata metadata) {
        super(Author.class, metadata);
    }

}

