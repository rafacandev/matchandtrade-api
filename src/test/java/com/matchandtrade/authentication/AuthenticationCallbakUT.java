package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
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
		UserAuthentication sessionUserAuthentication = mockFactory.nextRandomUserAuthentication();
		MockHttpServletRequest request = mockFactory.getHttpRquestWithAuthenticatedUser(sessionUserAuthentication);
		request.setParameter("state", "identicalStateMock");
		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "identicalStateMock");
		
		// AuthenticationCallbakServlet.AuthenticationOAuth is defined as AuthenticationOAuthTestExistingUserMock on matchandtrade-config.xml
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.doGet(request, response);

		UserAuthentication responseUserAuthentication = (UserAuthentication) request.getSession(false).getAttribute("user");
		assertNotNull(responseUserAuthentication.getUserId());
		assertTrue(responseUserAuthentication.isAuthenticated());
		assertEquals(AuthenticationOAuthExistingUserMock.EMAIL, responseUserAuthentication.getEmail());
		assertEquals(AuthenticationOAuthExistingUserMock.NAME, responseUserAuthentication.getName());
	}
	
}
