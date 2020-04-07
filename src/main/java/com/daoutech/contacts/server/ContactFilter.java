package com.daoutech.contacts.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.Getter;
import lombok.Setter;

import static com.daoutech.contacts.server.domain.QContact.contact;

@Getter
@Setter
public class ContactFilter {

	private Integer fCGroupId;
	private SearchType fSearchType;
	private String fKeyword = "";

	@JsonIgnore
	public BooleanBuilder getPredicate() {
		BooleanBuilder builder = new BooleanBuilder();
		if (this.fSearchType == null || this.fKeyword.isEmpty()) {
			return null;
		}

		StringPath searchField = contact.name;

		switch (this.fSearchType) {
			case TEL:
				searchField = contact.tel;
				break;
			case EMAIL:
				searchField = contact.email;
				break;
			case MEMO:
				searchField = contact.memo;
				break;
		}

		return builder.and(searchField.containsIgnoreCase(this.fKeyword));
	}

	enum SearchType {
		NAME, TEL, EMAIL, MEMO
	}
}