package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerGetIT {
	
	private UserController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getUserController(true);
		}
	}
	
	@Test(expected=RestException.class)
	public void getNegativeUnauthorized() {
		// The database is not supposed to hold negative userId. Therefore is a safe assumption to say that it will throw AuthorizationException
		fixture.get(-1);
	}
	
	@Test
	public void getPositive() {
		UserJson response = fixture.get(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		assertEquals(fixture.authenticationProvider.getAuthentication().getUser().getUserId(), response.getUserId());
	}

}
