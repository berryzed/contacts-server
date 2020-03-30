package com.daoutech.contacts.server;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.daoutech.contacts.server.domain.Contact;
import com.daoutech.contacts.server.domain.User;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1802170755L;

    public static final QUser user = new QUser("user");

    public final ListPath<Contact, QContact> contacts = this.<Contact, QContact>createList("contacts", Contact.class, QContact.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

