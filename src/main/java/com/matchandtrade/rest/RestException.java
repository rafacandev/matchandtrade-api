package com.matchandtrade.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {
	private static final long serialVersionUID = 4093425867051566908L;

	private Map<String, String> errors = new HashMap<>();
	private HttpStatus httpStatus;
	
	public RestException(HttpStatus httpStatus, String errorKey, String errorMessage) {
		super("RestException with httpStatus: " + httpStatus.toString() + " - " + httpStatus.getReasonPhrase() + "; errorKey: " + errorKey + "; errorMessage: " + errorMessage);
		this.httpStatus = httpStatus;
		this.errors.put(errorKey, errorMessage);
	}

	public RestException(HttpStatus httpStatus) {
		super("RestException with httpStatus: " + httpStatus.toString() + " - " + httpStatus.getReasonPhrase() + ".");
		this.httpStatus = httpStatus;
		this.errors.put(httpStatus.toString(), httpStatus.getReasonPhrase());
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
