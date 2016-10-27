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
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationCallbakUT {
	
	@Autowired
	AuthenticationProperties authenticationProperties;
	
	@Autowired
	AuthenticationCallback authenticationCallbakServlet;
	
	@Test
	public void doGetAtiForgeryTokenNegative() throws ServletException, IOException {
		AuthenticationCallback authenticationCallbakServlet = new AuthenticationCallback();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("state", "stateParameter");
		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "stateAttribute");
		MockHttpServletResponse response = new MockHttpServletResponse();
		authenticationCallbakServlet.doGet(request, response);
		assertEquals(401, response.getStatus());
		assertNull(request.getSession(false));
	}
	
	@Test
	public void doGetAtiForgeryTokenPositive() throws ServletException, IOException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("state", "identicalStateMock");
		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "identicalStateMock");
		
		// AuthenticationCallbakServlet.AuthenticationOAuth is defined as AuthenticationOAuthTestExistingUserMock on matchandtrade-config.xml
		authenticationCallbakServlet.doGet(request, response);

		UserAuthentication responseUserAuthentication = (UserAuthentication) request.getSession(false).getAttribute("user");
		assertNotNull(responseUserAuthentication.getUserId());
		assertTrue(responseUserAuthentication.isAuthenticated());
		assertEquals("AuthenticationOAuthTestExistingUserMock", responseUserAuthentication.getName());
		assertEquals("AuthenticationOAuthTestExistingUserMock@mock.com", responseUserAuthentication.getEmail());
	}
	
}
