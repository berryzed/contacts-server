package com.daoutech.contacts.server.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCGroup is a Querydsl query type for CGroup
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCGroup extends EntityPathBase<CGroup> {

    private static final long serialVersionUID = -1531372082L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCGroup cGroup = new QCGroup("cGroup");

    public final BooleanPath defalutGroup = createBoolean("defalutGroup");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final QUser user;

    public QCGroup(String variable) {
        this(CGroup.class, forVariable(variable), INITS);
    }

    public QCGroup(Path<? extends CGroup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCGroup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCGroup(PathMetadata metadata, PathInits inits) {
        this(CGroup.class, metadata, inits);
    }

    public QCGroup(Class<? extends CGroup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

