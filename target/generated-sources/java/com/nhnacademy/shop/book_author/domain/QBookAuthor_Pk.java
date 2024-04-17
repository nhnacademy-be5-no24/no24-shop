package com.nhnacademy.shop.book_author.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBookAuthor_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBookAuthor_Pk extends BeanPath<BookAuthor.Pk> {

    private static final long serialVersionUID = 463995559L;

    public static final QBookAuthor_Pk pk = new QBookAuthor_Pk("pk");

    public final NumberPath<Long> authorId = createNumber("authorId", Long.class);

    public final StringPath bookIsbn = createString("bookIsbn");

    public QBookAuthor_Pk(String variable) {
        super(BookAuthor.Pk.class, forVariable(variable));
    }

    public QBookAuthor_Pk(Path<? extends BookAuthor.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBookAuthor_Pk(PathMetadata metadata) {
        super(BookAuthor.Pk.class, metadata);
    }

}

