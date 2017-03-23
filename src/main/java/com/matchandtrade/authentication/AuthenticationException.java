package com.matchandtrade.authentication;

public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = -1791132770073106481L;
	
	public AuthenticationException(String message) {
		super(message);
	}
	
	public AuthenticationException(Exception exception) {
		super(exception);
	}
}
