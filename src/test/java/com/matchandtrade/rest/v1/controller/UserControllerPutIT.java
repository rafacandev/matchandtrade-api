package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.validator.ValidationException;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerPutIT {
	
	@Autowired
	private MockFactory mockFactory;
	@Autowired
	private UserController userController;
	
	private UserAuthentication userAuthentication;
	private MockHttpServletRequest httpRequest;

	@Before
	public void before() {
		// Let reuse userAuthentication and httpRequest to avoid unnecessary trips to the persistance layer
		if (userAuthentication == null) {
			userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		}
		if (httpRequest == null) {
			httpRequest = mockFactory.getHttpRequestWithAuthenticatedUser(userAuthentication);
		}
	}
	
	@Test(expected=AuthorizationException.class)
	public void putNegativeUnauthorizedResource() {
		// Trying to edit a resource of another user
		userController.setHttpServletRequest(httpRequest);
		UserJson requestJson = UserRandom.next();
		userController.put(-1, requestJson);
	}	
	
	@Test
	public void putPositive() {
		userController.setHttpServletRequest(httpRequest);
		UserJson requestJson = UserRandom.next();
		requestJson.setEmail(userAuthentication.getEmail());
		UserJson responseJson = userController.put(userAuthentication.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=ValidationException.class)
	public void putNegativeValidation() {
		userController.setHttpServletRequest(httpRequest);
		UserJson requestJson = UserRandom.next();
		userController.put(userAuthentication.getUserId(), requestJson);
	}

}
