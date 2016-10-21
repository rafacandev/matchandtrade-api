package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "/application-context-test.xml")
public class AuthenticationOAuthGoogleTest {
	
	private AuthenticationOAuthGoogle spyAuthenticationOAuthGoogle(String response)
			throws IOException, ClientProtocolException {
		ByteArrayInputStream httpResponseInputStream = new ByteArrayInputStream(response.getBytes());
		HttpEntity httpEntityMock = Mockito.mock(HttpEntity.class);
		Mockito
			.when(httpEntityMock.getContent())
			.thenReturn(httpResponseInputStream);

		// Mock CloseableHttpResponse
		CloseableHttpResponse closeableHttpResponseMock = Mockito.mock(CloseableHttpResponse.class);
		Mockito
			.when(closeableHttpResponseMock.getEntity())
			.thenReturn(httpEntityMock);
		
		// Spy AuthenticationOAuthGoogle
		AuthenticationOAuthGoogle authenticationOAuthGoogleSpy = Mockito.spy(AuthenticationOAuthGoogle.class);
		Mockito
			.doReturn(closeableHttpResponseMock)
			.when(authenticationOAuthGoogleSpy)
			.httpClientExecute(Matchers.any(), Matchers.any());
		return authenticationOAuthGoogleSpy;
	}
	
	@Test(expected=AuthenticationException.class)
	public void obtainAccessTokenNegative() throws ClientProtocolException, IOException {
		String response = "{ \"error\": \"invalid_request\", \"error_description\": \"Invalid parameter value for redirect_uri: Missing scheme: redirectURI\", \"error_uri\": \"\"}";
		AuthenticationOAuthGoogle authenticationOAuthGoogleSpy = spyAuthenticationOAuthGoogle(response);
		String codeParameter = "codeParameter";
		String clientId = "clientId";
		String clientSecret = "clientSecret";
		String redirectURI = "redirectURI";
		authenticationOAuthGoogleSpy.obtainAccessToken(codeParameter, clientId, clientSecret, redirectURI);
	}
	
	@Test
	public void obtainAccessTokenPositive() throws ClientProtocolException, IOException {
		String response = "{ \"access_token\":\"obtainAccessTokenPositive\" }";
		AuthenticationOAuthGoogle authenticationOAuthGoogleSpy = spyAuthenticationOAuthGoogle(response);
		String codeParameter = "codeParameter";
		String clientId = "clientId";
		String clientSecret = "clientSecret";
		String redirectURI = "redirectURI";
		String result = authenticationOAuthGoogleSpy.obtainAccessToken(codeParameter, clientId, clientSecret, redirectURI);
		assertEquals("obtainAccessTokenPositive", result);
	}
	
	@Test
	public void obtainUserInformation() throws ClientProtocolException, IOException {
		String email = "obtainUserInformation@test.com";
		String name = "nameTest";
		String response = "{ \"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
		AuthenticationOAuthGoogle authenticationOAuthGoogleSpy = spyAuthenticationOAuthGoogle(response);
		UserAuthentication result = authenticationOAuthGoogleSpy.obtainUserInformation("accessToken");
		assertEquals(email, result.getEmail());
		assertEquals(name, result.getName());
	}
	
	@Test
	public void redirectToAuthorizationAuthority() throws ClientProtocolException, IOException {
		String clientId = "clientId";
		String redirectURI = "redirectURI";
		String state = "state";
		MockHttpServletResponse responseMock = new MockHttpServletResponse();
		AuthenticationOAuthGoogle authenticationOAuthGoogleSpy = new AuthenticationOAuthGoogle(); 
		authenticationOAuthGoogleSpy.redirectToAuthorizationAuthority(responseMock, state, clientId, redirectURI);
		assertEquals("https://accounts.google.com/o/oauth2/v2/auth?client_id=clientId&response_type=code&scope=openid+email+profile&redirect_uri=redirectURI&state=state", responseMock.getRedirectedUrl());
	}

}
