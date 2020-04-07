package com.daoutech.contacts.server;

import com.querydsl.core.types.dsl.StringPath;

import static com.daoutech.contacts.server.domain.QContact.contact;

public enum DuplicateType {
	NAME, TEL, EMAIL;

	public StringPath getKey() {
		if (this == NAME) return contact.name;
		else if (this == EMAIL) return contact.email;
		else return contact.tel;
	}
}
