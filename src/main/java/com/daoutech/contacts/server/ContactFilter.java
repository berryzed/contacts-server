package com.daoutech.contacts.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Getter;
import lombok.Setter;

import static com.daoutech.contacts.server.QContact.contact;

@Getter
@Setter
public class ContactFilter {

	private SearchType fSearchType;
	private String fKeyword = "";

	public String generateRedisKey(String userId) {
		return userId + ":" + fSearchType + ":" + fKeyword;
	}

	@JsonIgnore
	public Predicate getPredicate() {
		if (this.fSearchType == null) return null;
		StringPath field = contact.name;

		switch (this.fSearchType) {
			case TEL:
				field = contact.tel;
				break;
			case EMAIL:
				field = contact.email;
				break;
//			case GROUP:
//				field = contact.tel;
//				break;
		}

		return field.containsIgnoreCase(this.fKeyword);
	}
}

enum SearchType {
	NAME, TEL, EMAIL, GROUP
}