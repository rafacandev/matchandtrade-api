package com.matchandtrade.authentication;

public class AuthenticationInfoJson {
	private String authenticationHeader;
	private String callbackUrl;

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

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(Object callbackUrl) {
		if (callbackUrl != null) {
			this.callbackUrl = callbackUrl.toString();
		} else {
			this.callbackUrl = null;
		}
	}

}
