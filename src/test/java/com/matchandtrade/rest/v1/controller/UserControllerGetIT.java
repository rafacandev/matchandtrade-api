package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerGetIT {
	
	@Autowired
	private MockFactory mockFactory;
	@Autowired
	private UserController userController;
	
	@Test(expected=AuthorizationException.class)
	public void getNegativeUnauthorized() {
		// the database is not supposed to hold negative userId. Therefore is a safe assumption to say that it will throw AuthorizationException
		userController.get(-1);
	}
	
	@Test
	public void getPositive() {
		UserAuthentication userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		MockHttpServletRequest httpRequest = mockFactory.getHttpRequestWithAuthenticatedUser(userAuthentication);
		userController.setHttpServletRequest(httpRequest);
		UserJson response = userController.get(userAuthentication.getUserId());
		assertEquals(userAuthentication.getUserId(), response.getUserId());
	}

}
