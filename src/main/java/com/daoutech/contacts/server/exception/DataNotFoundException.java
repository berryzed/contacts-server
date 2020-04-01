package com.daoutech.contacts.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException {

	public DataNotFoundException() {
		super("데이터를 찾을 수 없습니다.");
	}

	public DataNotFoundException(String message) {
		super(message);
	}
}
