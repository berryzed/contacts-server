package com.daoutech.contacts.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

	public AccessDeniedException() {
		super("권한이 없습니다.");
	}

	public AccessDeniedException(String message) {
		super(message);
	}
}
