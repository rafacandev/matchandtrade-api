package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationCallbakUT {
	
	@Autowired
	private AuthenticationCallback authenticationCallbakServlet;
	@Autowired
	private MockFactory mockFactory;
	
	@Test
	public void doGetAtiForgeryTokenNegative() throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("state", "differentState");
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.authenticate(request, response);
		assertEquals(401, response.getStatus());
		assertNull(request.getSession(false));
	}
	
	@Test
	public void doGetAtiForgeryTokenPositive() throws ServletException, IOException {
		String antiForgeryState = StringRandom.nextString();
		AuthenticationResponseJson sessionUserAuthentication = mockFactory.nextRandomUserAuthenticationPersisted(antiForgeryState);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), antiForgeryState);
		request.setParameter(AuthenticationProperties.OAuth.STATE_PARAMETER.toString(), antiForgeryState);
		
		AuthenticationOAuth authenticationOAuthMock = Mockito.mock(AuthenticationOAuth.class);
		Mockito.when(authenticationOAuthMock.obtainAccessToken(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(antiForgeryState);
		Mockito.when(authenticationOAuthMock.obtainUserInformation(Mockito.any())).thenReturn(sessionUserAuthentication);
		authenticationCallbakServlet.setAuthenticationOAuth(authenticationOAuthMock);
		
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.authenticate(request, response);
		String authenticationHeader = response.getHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString());
		assertEquals(antiForgeryState, authenticationHeader);
	}
	
}
