package com.matchandtrade.authentication;

/**
 * Enum to store supported actions related to authentication
 * 
 * @author rafael.santos.bra@gmail.com
 */
public enum AuthenticationAction {

	AUTHENTICATE, SIGNOUT;

	public static AuthenticationAction get(String s) {
		AuthenticationAction result = null;
		switch (s) {
		case "authenticate":
			result = AuthenticationAction.AUTHENTICATE;
			break;
		case "sign-out":
			result = AuthenticationAction.SIGNOUT;
			break;
		default:
			// There is no default value
			break;
		}
		return result;
	}
}
