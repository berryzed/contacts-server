package com.daoutech.contacts.server;

import com.querydsl.core.types.dsl.StringPath;

import static com.daoutech.contacts.server.domain.QContact.contact;

public enum DuplicateType {
    TEL, EMAIL;

    public StringPath getKey() {
        if (this == EMAIL) return contact.email;
        else return contact.tel;
    }
}
