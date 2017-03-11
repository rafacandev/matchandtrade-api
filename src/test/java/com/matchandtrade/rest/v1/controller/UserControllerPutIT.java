package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
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
	
	@Test(expected=ValidationException.class)
	public void putNegativeUnauthorizedResource() {
		// Trying to edit a resource of another user
		UserJson requestJson = UserRandom.nextJson();
		userController.put(-1, requestJson);
	}	
	
	@Test
	public void putPositive() {
		UserEntity userEntity = UserRandom.nextEntity();
		AuthenticationEntity authenticationEntity = mockFactory.getAuthentication(userEntity);
		MockHttpServletRequest request = mockFactory.getAuthenticatedRequest(authenticationEntity);
		userController.setHttpServletRequest(request);
		
		UserJson requestJson = UserRandom.nextJson();
		requestJson.setEmail(userEntity.getEmail());
		UserJson responseJson = userController.put(authenticationEntity.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}

}
