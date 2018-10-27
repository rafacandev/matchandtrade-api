package com.matchandtrade.authentication;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationOAuthGoogleUT {

	private AuthenticationOAuthGoogle spyAuthenticationOAuthGoogle(String response)	throws IOException {
		ByteArrayInputStream httpResponseInputStream = new ByteArrayInputStream(response.getBytes());
		HttpEntity httpEntityMock = mock(HttpEntity.class);
		doReturn(httpResponseInputStream).when(httpEntityMock).getContent();

		// Mock CloseableHttpResponse
		CloseableHttpResponse closeableHttpResponseMock = mock(CloseableHttpResponse.class);
		doReturn(httpEntityMock).when(closeableHttpResponseMock).getEntity();

		// Spy AuthenticationOAuthGoogle
		AuthenticationOAuthGoogle authenticationOAuthGoogleSpy = spy(AuthenticationOAuthGoogle.class);
		doReturn(closeableHttpResponseMock).when(authenticationOAuthGoogleSpy).httpClientExecute(any(), any());

		return authenticationOAuthGoogleSpy;
	}

	@Test(expected=AuthenticationException.class)
	public void obtainAccessTokenNegative() throws ClientProtocolException, IOException {
		String response = "{ \"error\": \"invalid_request\", \"error_description\": \"Invalid parameter value for redirect_uri: Missing scheme: redirectURI\", \"error_uri\": \"\"}";
		AuthenticationOAuthGoogle fixture = spyAuthenticationOAuthGoogle(response);
		String codeParameter = "codeParameter";
		String clientId = "clientId";
		String clientSecret = "clientSecret";
		String redirectURI = "redirectURI";
		fixture.obtainAccessToken(codeParameter, clientId, clientSecret, redirectURI);
	}
	
	@Test
	public void obtainAccessTokenPositive() throws ClientProtocolException, IOException {
		String response = "{ \"access_token\":\"obtainAccessTokenPositive\" }";
		AuthenticationOAuthGoogle fixture = spyAuthenticationOAuthGoogle(response);
		String codeParameter = "codeParameter";
		String clientId = "clientId";
		String clientSecret = "clientSecret";
		String redirectURI = "redirectURI";
		String result = fixture.obtainAccessToken(codeParameter, clientId, clientSecret, redirectURI);
		assertEquals("obtainAccessTokenPositive", result);
	}
	
	@Test
	public void obtainUserInformation() throws ClientProtocolException, IOException {
		String email = "obtainUserInformation@test.com";
		String name = "nameTest";
		String response = "{ \"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
		AuthenticationOAuthGoogle fixture = spyAuthenticationOAuthGoogle(response);
		AuthenticationResponsePojo result = fixture.obtainUserInformation("accessToken");
		assertEquals(email, result.getEmail());
		assertEquals(name, result.getName());
	}
	
	@Test
	public void redirectToAuthorizationAuthority() throws ClientProtocolException, IOException {
		String clientId = "clientId";
		String redirectURI = "redirectURI";
		String state = "state";
		MockHttpServletResponse responseMock = new MockHttpServletResponse();
		AuthenticationOAuthGoogle fixture = new AuthenticationOAuthGoogle();
		fixture.redirectToAuthorizationAuthority(responseMock, state, clientId, redirectURI);
		assertEquals("https://accounts.google.com/o/oauth2/v2/auth?client_id=clientId&response_type=code&scope=openid+email+profile&redirect_uri=redirectURI&state=state", responseMock.getRedirectedUrl());
	}

}
