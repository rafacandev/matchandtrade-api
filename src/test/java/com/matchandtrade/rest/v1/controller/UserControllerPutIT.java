package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.v1.controller.MockUserControllerFactory.MockUserController;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.validator.ValidationException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerPutIT {
	
	@Autowired
	private MockUserControllerFactory mockUserControllerFactory;
	private MockUserController fixture;

	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockUserControllerFactory.getMockUserController();
		}
	}
	
	@Test(expected=ValidationException.class)
	public void putNegativeUnauthorizedResource() {
		// Trying to edit a resource of another user
		UserJson requestJson = UserRandom.nextJson();
		fixture.put(-1, requestJson);
	}	
	
	@Test
	public void putPositive() {
		UserJson requestJson = UserRandom.nextJson();
		requestJson.setEmail(fixture.userEntity.getEmail());
		UserJson responseJson = fixture.put(fixture.userEntity.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}

}
