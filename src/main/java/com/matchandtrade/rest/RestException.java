package com.matchandtrade.rest;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {

	private static final long serialVersionUID = 4093425867051566908L;

	private String description;
	private HttpStatus httpStatus;
	
	public RestException(HttpStatus httpStatus) {
		super(httpStatus.getReasonPhrase());
		this.httpStatus = httpStatus;
	}

	public RestException(HttpStatus httpStatus, String description) {
		super("HTTP Status " + httpStatus.value() + " " + httpStatus.getReasonPhrase() + ": " + description);
		this.httpStatus = httpStatus;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
