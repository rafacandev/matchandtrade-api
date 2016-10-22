package com.matchandtrade.authorization;

public class AuthorizationException extends RuntimeException {
	private static final long serialVersionUID = -5429880216552705363L;
	
	public enum Type {
		USER_NOT_REGISTERED,
		USER_NOT_AUTHORIZED
	}
	
	public AuthorizationException(Type type) {
		super(type.toString());
	}

}
