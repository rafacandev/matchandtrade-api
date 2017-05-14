package com.matchandtrade.config;

public class AuthenticationProperties {
	
	public enum OAuth {
		ANTI_FORGERY_STATE("anti-forgery-state"),
		CODE_PARAMETER("code"),
		STATE_PARAMETER("state"),
		AUTHORIZATION_HEADER("Authorization");
		
		String text;
		OAuth(String t) {
			this.text = t;
		}
		
		@Override
		public String toString() {
			return this.text;
		}
	}
	
	public enum Action {
		AUTHENTICATE, CALLBACK, SIGNOUT;
		public static Action get(String s) {
			Action result = null;
			switch (s) {
			case "authenticate":
				result = Action.AUTHENTICATE;
				break;
			case "sign-out":
				result = Action.SIGNOUT;
				break;
			case "callback":
				result = Action.CALLBACK;
				break;			
			default:
				// There is no default value
				break;
			}
			return result;
		}
	}
	
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
