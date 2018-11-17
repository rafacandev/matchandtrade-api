package com.matchandtrade.authentication;

public enum AuthenticationAction {

	AUTHENTICATE("authenticate"), CALLBACK("callback"), SIGN_OFF("sign-off"), INFO("info");

	private String alias;

	AuthenticationAction(String alias) {
		this.alias = alias;
	}

	public static AuthenticationAction fromAlias(String alias) {
		for (AuthenticationAction action : values()) {
			if (action.alias.equals(alias)) {
				return action;
			}
		}
		return null;
	}

}
