package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationCallbakUT {
	
	@Autowired
	private AuthenticationCallback authenticationCallbakServlet;
	@Autowired
	private MockFactory mockFactory;
	
	@Test
	public void doGetAtiForgeryTokenNegative() throws ServletException, IOException {
		AuthenticationCallback authenticationCallbakServlet = new AuthenticationCallback();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("state", "differentState");
		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "stateDifferent");
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.doGet(request, response);
		assertEquals(401, response.getStatus());
		assertNull(request.getSession(false));
	}
	
	@Test
	public void doGetAtiForgeryTokenPositive() throws ServletException, IOException {
		UserAuthentication sessionUserAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		MockHttpServletRequest request = mockFactory.getHttpRquestWithAuthenticatedUser(sessionUserAuthentication);
		request.setParameter("state", "identicalStateMock");
		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "identicalStateMock");
		
		AuthenticationOAuth authenticationOAuthMock = Mockito.mock(AuthenticationOAuth.class);
		Mockito.when(authenticationOAuthMock.obtainAccessToken(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("accessTokenMock");
		Mockito.when(authenticationOAuthMock.obtainUserInformation("accessTokenMock")).thenReturn(sessionUserAuthentication);
		authenticationCallbakServlet.setAuthenticationOAuth(authenticationOAuthMock);

		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.doGet(request, response);

		UserAuthentication responseUserAuthentication = (UserAuthentication) request.getSession(false).getAttribute("user");
		assertNotNull(responseUserAuthentication.getUserId());
		assertTrue(responseUserAuthentication.isAuthenticated());
		assertEquals(sessionUserAuthentication.getEmail(), responseUserAuthentication.getEmail());
		assertEquals(sessionUserAuthentication.getName(), responseUserAuthentication.getName());
	}
	
}
