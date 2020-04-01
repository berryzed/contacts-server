package com.daoutech.contacts.server.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ErrorResponse implements Serializable {

	private String error;
	private String description;

	public ErrorResponse(Exception e) {
		this.error = e.getClass().getSimpleName();
		this.description = e.getMessage();
	}

	public ErrorResponse(Exception e, String description) {
		this.error = e.getClass().getSimpleName();
		this.description = description;
	}
}
