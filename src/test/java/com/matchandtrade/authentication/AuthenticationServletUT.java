package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

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
public class AuthenticationServletUT {
	
	@Autowired
	AuthenticationProperties authenticationProperties;
	
	@Autowired
	AuthenticationServlet authenticationServlet;
	
	@Test
	public void authenticationAction() {
		MockHttpServletRequest requestMock = new MockHttpServletRequest();
		requestMock.setRequestURI("http://localhost:8080/authenticate");
		
		AuthenticationServlet authenticationServlet = new AuthenticationServlet();
		AuthenticationProperties.Action authenticationAction = authenticationServlet.obtainAuthenticationAction(requestMock);
		assertEquals(AuthenticationProperties.Action.AUTHENTICATE, authenticationAction);
		
		requestMock.setRequestURI("http://localhost:8080/authenticate/");
		authenticationAction = authenticationServlet.obtainAuthenticationAction(requestMock);
		assertEquals(AuthenticationProperties.Action.AUTHENTICATE, authenticationAction);

		requestMock.setRequestURI("http://localhost:8080/sign-out");
		authenticationAction = authenticationServlet.obtainAuthenticationAction(requestMock);
		assertEquals(AuthenticationProperties.Action.SIGNOUT, authenticationAction);
	}
	
	@Test
	public void doGetAuthenticateExistingUserMock() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		authenticationServlet.setAuthenticationOAuth(new AuthenticationOAuthNewUserMock());
		authenticationServlet.doGet(request, response);
		
		URI uri = new URI(response.getRedirectedUrl());
		String redirectUrl = uri.getScheme() +"://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
		assertEquals(authenticationProperties.getRedirectURI(), redirectUrl);
	}
	
	@Test
	public void doGetSignOff() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate/sign-out");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		authenticationServlet.doGet(request, response);
		
		assertNull(request.getSession(false));
	}

	@Test
	public void doGetInvalidUrl() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate/invalid");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		authenticationServlet.doGet(request, response);
		
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void doGetCallback() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate/callback");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		authenticationServlet.doGet(request, response);
		
		assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}
	
}