package com.matchandtrade.authentication;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "/application-context-test.xml")
public class AuthenticationOAuthGoogleTest {
	
	@Test(expected=AuthenticationException.class)
	public void obtainAccessToken() throws ClientProtocolException, IOException {
		// Mock HttpEntity
		String response = "{ \"error\": \"invalid_request\", \"error_description\": \"Invalid parameter value for redirect_uri: Missing scheme: redirectURI\", \"error_uri\": \"\"}";
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
			.httpClientExecutePost(Matchers.any(), Matchers.any());
		
		String codeParameter = "codeParameter";
		String clientId = "clientId";
		String clientSecret = "clientSecret";
		String redirectURI = "redirectURI";
		
		authenticationOAuthGoogleSpy.obtainAccessToken(codeParameter, clientId, clientSecret, redirectURI);
	}

}
