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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AuthenticationProperties;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "/application-context-test.xml")
public class AuthenticationServletTest {
	
	@Autowired
	AuthenticationProperties authenticationProperties;
	
	@Autowired
	AuthenticationServlet authenticationServlet;
	
	@Test
	public void authenticationAction() {
		MockHttpServletRequest requestMock = new MockHttpServletRequest();
		requestMock.setRequestURI("http://localhost:8080/authenticate");
		
		AuthenticationServlet authenticationServlet = new AuthenticationServlet();
		AuthenticationAction authenticationAction = authenticationServlet.getAuthenticationAction(requestMock);
		assertEquals(AuthenticationAction.AUTHENTICATE, authenticationAction);
		
		requestMock.setRequestURI("http://localhost:8080/authenticate/");
		authenticationAction = authenticationServlet.getAuthenticationAction(requestMock);
		assertEquals(AuthenticationAction.AUTHENTICATE, authenticationAction);

		requestMock.setRequestURI("http://localhost:8080/sign-out");
		authenticationAction = authenticationServlet.getAuthenticationAction(requestMock);
		assertEquals(AuthenticationAction.SIGNOUT, authenticationAction);
	}
	
	@Test
	public void doGetAuthenticate() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		authenticationServlet.doGet(request, response);
		
		URI uri = new URI(response.getRedirectedUrl());
		String redirectUrl = uri.getScheme() +"://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
		assertEquals("http://localhost:8080/authenticate/callback", redirectUrl);
	}
	
	@Test
	public void doGetSignOff() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate/sign-out");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		AuthenticationServlet authenticationServlet = new AuthenticationServlet();
		authenticationServlet.doGet(request, response);
		
		assertNull(request.getSession(false));
	}

	@Test
	public void doGetInvalidUrl() throws ServletException, IOException, URISyntaxException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost:8080/authenticate/invalid");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		AuthenticationServlet authenticationServlet = new AuthenticationServlet();
		authenticationServlet.doGet(request, response);
		
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	}
	
}