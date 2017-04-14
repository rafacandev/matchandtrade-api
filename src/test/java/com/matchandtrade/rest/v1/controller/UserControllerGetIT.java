package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.controller.MockUserControllerFactory.MockUserController;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerGetIT {
	
	@Autowired
	private MockUserControllerFactory mockUserControllerFactory;
	private MockUserController fixture;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockUserControllerFactory.getMockUserController();
		}
	}
	
	@Test(expected=AuthorizationException.class)
	public void getNegativeUnauthorized() {
		// the database is not supposed to hold negative userId. Therefore is a safe assumption to say that it will throw AuthorizationException
		fixture.get(-1);
	}
	
	@Test
	public void getPositive() {
		UserJson response = fixture.get(fixture.userEntity.getUserId());
		assertEquals(fixture.userEntity.getUserId(), response.getUserId());
	}

	@Test(expected=AuthorizationException.class)
	public void getNegative() {
		UserJson response = fixture.get(-1);
		assertNull(response);
	}

}
