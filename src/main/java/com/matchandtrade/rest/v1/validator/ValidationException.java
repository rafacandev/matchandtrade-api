package com.matchandtrade.rest.v1.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 6446956628690195866L;

	public enum ErrorType {
		MANDATORY_PARAMETER, INVALID_OPERATION, UNIQUE_PARAMETER
	}

	private Map<ErrorType, String> errors = new HashMap<>();
	
	public ValidationException(ErrorType errorType, String errorMessage) {
		super("ValidationException with errorType: " + errorType.toString() + "; errorMessage: " + errorMessage);
		errors.put(errorType, errorMessage);
	}
	
	public Map<ErrorType, String> getErrors() {
		return errors;
	}
	
}
