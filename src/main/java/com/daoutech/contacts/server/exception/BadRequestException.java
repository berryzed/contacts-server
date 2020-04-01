package com.daoutech.contacts.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	public BadRequestException() {
		super("잘못된 요청입니다.");
	}

	public BadRequestException(String message) {
		super(message);
	}
}
