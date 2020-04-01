package com.daoutech.contacts.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BearerTokenException extends RuntimeException {

	public BearerTokenException() {
		super("인증되지 않은 요청입니다.");
	}

	public BearerTokenException(String message) {
		super(message);
	}
}
