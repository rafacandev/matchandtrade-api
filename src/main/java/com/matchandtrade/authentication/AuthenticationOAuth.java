package com.matchandtrade.authentication;

import javax.servlet.http.HttpServletResponse;

public interface AuthenticationOAuth {
	
	public String obtainAccessToken(
			String codeParameter,
			String clientId,
			String clientSecret,
			String redirectURI) throws AuthenticationException;

	public AuthenticationResponsePojo obtainUserInformation(String accessToken) throws AuthenticationException;
	
	public void redirectToAuthorizationAuthority(
			HttpServletResponse response,
			String state,
			String clientId,
			String redirectURI) throws AuthenticationException;
}
