package com.daoutech.contacts.server;

import com.querydsl.core.types.dsl.StringPath;

import static com.daoutech.contacts.server.QContact.contact;

public enum DuplicateType {
	NAME, TEL, EMAIL;

	public static StringPath getKey(DuplicateType type) {
		if (type == NAME) return contact.name;
		else if (type == EMAIL) return contact.email;
		else return contact.tel;
	}
}
