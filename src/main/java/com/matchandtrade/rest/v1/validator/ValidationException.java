package com.matchandtrade.rest.v1.validator;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 6446956628690195866L;

	public enum ErrorType {
		MANDATORY_PARAMETER, INVALID_OPERATION, UNIQUE_PARAMETER, RESOURCE_NOT_FOUND
	}

	private ErrorType errorType;
	
	public ValidationException(ErrorType errorType, String errorMessage) {
		super(errorType.toString() + ": " + errorMessage);
		this.errorType = errorType;
	}
	
	public ErrorType getErrorType() {
		return errorType;
	}
	
}
