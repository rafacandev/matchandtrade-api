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
import com.matchandtrade.test.IntegrationTestStore;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerPutIT {
	
	@Autowired
	private MockFactory mockFactory;
	private UserAuthentication userAuthentication;
	@Autowired
	private UserController userController;
	
	@Before
	public void beforeClass() {
		userAuthentication = mockFactory.nextRandomUserAuthentication();
		IntegrationTestStore.add(IntegrationTestStore.StoredObject.UserAuthentication, userAuthentication);
	}
	
	@Test(expected=AuthorizationException.class)
	public void putNegativeUnauthorizedResource() {
		// Trying to edit a resource of another user
		MockHttpServletRequest httpRequest = mockFactory.getHttpRquestWithAuthenticatedUserFromIntegrationTestStore();
		userController.setHttpServletRequest(httpRequest);
		UserJson requestJson = UserRandom.next();
		userController.put(-1, requestJson);
	}	
	
	@Test
	public void putPositive() {
		MockHttpServletRequest httpRequest = mockFactory.getHttpRquestWithAuthenticatedUserFromIntegrationTestStore();
		userController.setHttpServletRequest(httpRequest);
		UserJson requestJson = UserRandom.next();
		requestJson.setEmail(userAuthentication.getEmail());
		UserJson responseJson = userController.put(userAuthentication.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=ValidationException.class)
	public void putNegativeValidation() {
		MockHttpServletRequest httpRequest = mockFactory.getHttpRquestWithAuthenticatedUserFromIntegrationTestStore();
		userController.setHttpServletRequest(httpRequest);
		UserJson requestJson = UserRandom.next();
		userController.put(userAuthentication.getUserId(), requestJson);
	}

}
