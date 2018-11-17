package com.matchandtrade.authentication;

import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServletUT {

	@Mock
	private AuthenticationCallback authenticationCallbackMock;
	@Mock
	private AuthenticationRespositoryFacade authenticationRepositoryFacadeMock;
	private AuthenticationServlet fixture;
	private URI redirectUri;
	private MockHttpServletRequest requestMock;
	private MockHttpServletResponse responseMock;

	@Before
	public void before() throws URISyntaxException {
		fixture = new AuthenticationServlet();
		redirectUri = new URI(ConfigurationPropertiesMocker.AUTHENTICATION_REDIRECT_URL);
		fixture.authenticationRepository = authenticationRepositoryFacadeMock;
		fixture.authenticationCallbak = authenticationCallbackMock;
		fixture.authenticationOAuth = new AuthenticationOAuthNewUserMock();
		fixture.configProperties = ConfigurationPropertiesMocker.buildConfigProperties();
		requestMock = new MockHttpServletRequest();
		responseMock = new MockHttpServletResponse();
	}
	
	@Test
	public void authenticationAction() {
		MockHttpServletRequest requestMock = new MockHttpServletRequest();
		requestMock.setRequestURI("http://localhost:8080/authenticate");

		AuthenticationServlet fixture = new AuthenticationServlet();
		AuthenticationAction authenticationAction = fixture.obtainAuthenticationAction(requestMock);
		assertEquals(AuthenticationAction.AUTHENTICATE, authenticationAction);
		
		requestMock.setRequestURI("http://localhost:8080/authenticate/");
		authenticationAction = fixture.obtainAuthenticationAction(requestMock);
		assertEquals(AuthenticationAction.AUTHENTICATE, authenticationAction);

		requestMock.setRequestURI("http://localhost:8080/sign-off");
		authenticationAction = fixture.obtainAuthenticationAction(requestMock);
		assertEquals(AuthenticationAction.SIGN_OFF, authenticationAction);
	}
	
	@Test
	public void doGetAuthenticateExistingUserMock() throws ServletException, IOException, URISyntaxException {
		requestMock.setRequestURI("http://localhost:8080/authenticate");
		fixture.doGet(requestMock, responseMock);
		URI actualUri = new URI(responseMock.getRedirectedUrl());
		assertEquals(redirectUri.getHost(), actualUri.getHost());
		assertEquals(redirectUri.getScheme(), actualUri.getScheme());
		assertTrue(actualUri.getQuery().contains("client_id=" + ConfigurationPropertiesMocker.AUTHENTICATION_CLIENT_ID));
		assertTrue(actualUri.getQuery().contains("response_type=code"));
		assertTrue(actualUri.getQuery().contains("scope=openid+email+profile"));
		assertTrue(actualUri.getQuery().contains("redirect_uri="+ConfigurationPropertiesMocker.AUTHENTICATION_REDIRECT_URL+"&state"));
	}
	
	@Test
	public void doGetSignOffWithoutToken() throws ServletException, IOException, URISyntaxException {
		requestMock.setRequestURI("http://localhost:8080/authenticate/sign-out");
		fixture.doGet(requestMock, responseMock);
		assertNull(requestMock.getSession(false));
	}

	@Test
	public void doGetSignOffWithToken() throws ServletException, IOException, URISyntaxException {
		requestMock.setRequestURI("http://localhost:8080/authenticate/sign-out");
		fixture.doGet(requestMock, responseMock);
		assertNull(requestMock.getSession(false));
	}

	@Test
	public void doGetInvalidUrl() throws ServletException, IOException, URISyntaxException {
		requestMock.setRequestURI("http://localhost:8080/authenticate/invalid");
		fixture.doGet(requestMock, responseMock);
		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), responseMock.getStatus());
	}
	
	@Test
	public void doGetUnauthorizedCallback() throws ServletException, IOException, URISyntaxException {
		requestMock.setRequestURI("http://localhost:8080/authenticate/callback");
		fixture.doGet(requestMock, responseMock);
		verify(authenticationCallbackMock, times(1)).authenticate(any(), any());
	}
	
}