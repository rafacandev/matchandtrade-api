package com.matchandtrade.authentication;

public class AuthenticationInfoJson {
	private String authenticationHeader;

	public String getAuthenticationHeader() {
		return authenticationHeader;
	}

	public void setAuthenticationHeader(Object authenticationHeader) {
		if (authenticationHeader != null) {
			this.authenticationHeader = authenticationHeader.toString();
		} else {
			this.authenticationHeader = null;
		}
	}

}
