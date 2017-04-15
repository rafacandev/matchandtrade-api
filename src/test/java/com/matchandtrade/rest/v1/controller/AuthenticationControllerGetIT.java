package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationControllerGetIT {

	@Autowired
	AuthenticationController fixture;
	@Autowired
	MockControllerFactory mockControllerFactory;
	
	@Test(expected=AuthorizationException.class)
	public void getNegative() {
		// Should fail as there is not authenticated user
		fixture.get();
	}
	
	@Test
	public void getPositive() {
		AuthenticationController authenticatedFixture = mockControllerFactory.getAuthenticationController();
		AuthenticationJson response = authenticatedFixture.get();
		Assert.assertEquals(authenticatedFixture.authenticationProvider.getAuthentication().getUserId(), response.getUserId());
	}
	
}
