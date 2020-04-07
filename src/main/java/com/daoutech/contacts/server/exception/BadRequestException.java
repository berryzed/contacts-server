package com.daoutech.contacts.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	private static final String MSG = "잘못된 요청입니다.";

	private List<ErrorResponse.Field> errors;

	public BadRequestException() {
		super("잘못된 요청입니다.");
	}

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(ErrorResponse.Field field) {
		super("잘못된 요청입니다.");
		this.errors = new ArrayList<>();
		this.errors.add(field);
	}
}
