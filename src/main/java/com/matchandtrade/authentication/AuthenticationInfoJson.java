package com.matchandtrade.authentication;

public class AuthenticationInfoJson {
	private String authorizationHeader;

	public String getAuthorizationHeader() {
		return authorizationHeader;
	}

	public void setAuthorizationHeader(Object authorizationHeader) {
		if (authorizationHeader != null) {
			this.authorizationHeader = authorizationHeader.toString();
		} else {
			this.authorizationHeader = null;
		}
	}
}
