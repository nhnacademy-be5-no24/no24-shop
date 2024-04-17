package com.nhnacademy.shop.grade.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGrade is a Querydsl query type for Grade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGrade extends EntityPathBase<Grade> {

    private static final long serialVersionUID = -813752621L;

    public static final QGrade grade = new QGrade("grade");

    public final NumberPath<Long> accumulateRate = createNumber("accumulateRate", Long.class);

    public final NumberPath<Long> gradeId = createNumber("gradeId", Long.class);

    public final StringPath gradeName = createString("gradeName");

    public QGrade(String variable) {
        super(Grade.class, forVariable(variable));
    }

    public QGrade(Path<? extends Grade> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGrade(PathMetadata metadata) {
        super(Grade.class, metadata);
    }

}

