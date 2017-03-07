package com.matchandtrade.config;

public class AuthenticationProperties {
	
	public static enum Token {
		ANTI_FORGERY_STATE
	}
	
	public static final String AUTHENTICATION_HEADER = "Authentication";
	
	private String clientId;
	private String clientSecret;
	private String redirectURI;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectURI() {
		return redirectURI;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

}
