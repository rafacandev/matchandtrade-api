package com.matchandtrade.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;

import com.matchandtrade.persistence.repository.UserRepository;
import org.h2.engine.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationCallbakUT {
	
	private AuthenticationCallback fixture;
	private MockHttpServletRequest requestMock;
	private MockHttpServletResponse responseMock;
	@Mock
	private AuthenticationOAuth authenticationOAuthMock;
	@Mock
	private UserRepositoryFacade userRepositoryFacadeMock;
	private String antiForgeryState = "antiForgeryState";

	@Mock
	protected AuthenticationRespositoryFacade authenticationRepositoryFacadeMock;
	@Before
	public void before() {
		fixture = new AuthenticationCallback();
		String defaultStateValue = "defaultStateValue";

		when(authenticationRepositoryFacadeMock.findByAtiForgeryState(defaultStateValue)).thenReturn(new AuthenticationEntity());
		fixture.authenticationRepository = authenticationRepositoryFacadeMock;

		when(userRepositoryFacadeMock.findByEmail(any())).thenReturn(new UserEntity());
		fixture.userRepository = userRepositoryFacadeMock;

		AuthenticationResponsePojo sessionUserAuthentication = new AuthenticationResponsePojo(null, null, null, null, null);
		when(authenticationOAuthMock.obtainAccessToken(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(antiForgeryState);
		when(authenticationOAuthMock.obtainUserInformation(Mockito.any())).thenReturn(sessionUserAuthentication);
		fixture.authenticationOAuth = authenticationOAuthMock;

		AuthenticationProperties authenticationProperties = new AuthenticationProperties();
		authenticationProperties.setCallbackUrl("http://test.com");
		authenticationProperties.setSessionTimeout(1);
		fixture.authenticationProperties = authenticationProperties;

		requestMock = new MockHttpServletRequest();
		requestMock.setParameter("state", defaultStateValue);
		requestMock.setSession(new MockHttpSession());
		responseMock = new MockHttpServletResponse();
	}

	@Test
	public void authenticate_When_StateParameterDoesNotMatch_Then_ResponseStatusIs401AndSessionIsNull() throws IOException {
		requestMock.setParameter("state", "unknownStateParameter");
		fixture.authenticate(requestMock, responseMock);
		assertEquals(401, responseMock.getStatus());
		assertNull(requestMock.getSession(false));
	}

	@Test
	public void authenticate_When_HappyPath_Then_Succeeds() throws IOException {
		fixture.authenticate(requestMock, responseMock);
		String authenticationHeader = responseMock.getHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString());
		assertEquals(antiForgeryState, authenticationHeader);
	}

}
