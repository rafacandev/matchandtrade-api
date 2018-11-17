package com.matchandtrade.authentication;

import javax.servlet.http.HttpServletResponse;

public interface AuthenticationOAuth {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	String obtainAccessToken(
		String codeParameter,
		String clientId,
		String clientSecret,
		String redirectURI) throws AuthenticationException;

	AuthenticationResponsePojo obtainUserInformation(String accessToken) throws AuthenticationException;
	
	void redirectToAuthorizationAuthority(
		HttpServletResponse response,
		String state,
		String clientId,
		String redirectURI) throws AuthenticationException;
	}
