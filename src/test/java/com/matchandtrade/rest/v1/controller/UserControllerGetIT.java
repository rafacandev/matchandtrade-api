package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
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
	private AuthenticationEntity authenticationEntity;
	
	@Before
	public void before() {
		authenticationEntity = mockFactory.getAuthentication();
		MockHttpServletRequest request = mockFactory.getAuthenticatedRequest(authenticationEntity);
		userController.setHttpServletRequest(request);
	}
	
	@Test(expected=AuthorizationException.class)
	public void getNegativeUnauthorized() {
		// the database is not supposed to hold negative userId. Therefore is a safe assumption to say that it will throw AuthorizationException
		userController.get(-1);
	}
	
	@Test
	public void getPositive() {
		UserJson response = userController.get(authenticationEntity.getUserId());
		assertEquals(authenticationEntity.getUserId(), response.getUserId());
	}

}
