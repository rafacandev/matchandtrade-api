package com.matchandtrade.authorization;

public class AuthorizationException extends RuntimeException {
	private static final long serialVersionUID = 2037728646255985447L;
	
	/**
	 * UNAUTHORIZED equivalent to HTTP 401 Unauthorized: The request requires user authentication.
	 * FORBIDDEN equivalent to HTTP 403 Forbidden: The server understood the request, but is refusing to fulfill it.
	 * @author rafael.santos.bra@gmail.com
	 */
	public enum Type {
		FORBIDDEN, UNAUTHORIZED
	}
	
	private Type type;
	
	public AuthorizationException(Type type) {
		super("AuthorizationException type: " + type.toString());
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

}
