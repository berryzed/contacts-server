package com.daoutech.contacts.server.exception;

import lombok.*;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse implements Serializable {

	private String error;
	private String description;
	private List<Field> errors;

	public ErrorResponse(Exception e) {
		this.error = e.getClass().getSimpleName();
		this.description = e.getMessage();
	}

	public ErrorResponse(Exception e, String description) {
		this.error = e.getClass().getSimpleName();
		this.description = description;
	}

	public ErrorResponse(Exception e, List<Field> errors) {
		this.error = e.getClass().getSimpleName();
		this.description = e.getMessage();
		this.errors = errors;
	}

	@Getter
	@Setter
	public static class Field implements Serializable {

		private String field;
		private String errorCode;
		private String message;

		public Field(String field, String message) {
			this.field = field;
			this.errorCode = "error." + field;
			this.message = message;
		}

		public Field(FieldError error, String message) {
			this.field = error.getField();
			this.errorCode = "error." + error.getField();
			this.message = message;
		}

		public Field(FieldError error) {
			this.field = error.getField();
			this.errorCode = "error." + error.getField();
			this.message = error.getDefaultMessage();
		}
	}
}
