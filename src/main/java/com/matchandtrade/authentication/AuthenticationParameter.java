package com.matchandtrade.authentication;

public enum AuthenticationParameter {
	CALLBACK_URL("callbackUrl");
	private String text;

	AuthenticationParameter(String text) {
			this.text = text;
		}

	@Override
	public String toString() {
		return text;
	}
}
