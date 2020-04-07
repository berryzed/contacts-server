package com.daoutech.contacts.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	@ExceptionHandler(Exception.class)
//	protected ErrorResponse handleDefaultException(Exception e) {
//		return new ErrorResponse(e, "알 수 없는 에러가 발생하였습니다.");
//	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(BearerTokenException.class)
	protected ErrorResponse handleBearerTokenException(BearerTokenException e) {
		return new ErrorResponse(e);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return new ErrorResponse(e, "잘못된 파라미터 요청입니다.");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	protected ErrorResponse handleBadRequestException(BadRequestException e) {
		return new ErrorResponse(e, e.getErrors());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(DataNotFoundException.class)
	protected ErrorResponse handleDataNotFoundException(DataNotFoundException e) {
		return new ErrorResponse(e);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
		return new ErrorResponse(e);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		List<ErrorResponse.Field> errors = new ArrayList<>();
		for (FieldError fe : e.getBindingResult().getFieldErrors()) {
			errors.add(new ErrorResponse.Field(fe));
		}
		return ErrorResponse.builder()
				.error("MethodArgumentNotValidException")
				.description("잘못된 타입 요청입니다.")
				.errors(errors)
				.build();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	protected ErrorResponse handleBindException(BindException e) {
		List<ErrorResponse.Field> errors = new ArrayList<>();
		for (FieldError fe : e.getFieldErrors()) {
			String message = String.format("[%s] 잘못된 타입의 값입니다.", fe.getRejectedValue());
			errors.add(new ErrorResponse.Field(fe, message));
		}
		return ErrorResponse.builder()
				.error("BindException")
				.description("잘못된 타입 요청입니다.")
				.errors(errors)
				.build();
	}
}
