package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationControllerGetIT {

	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Test
	public void getPositive() {
		AuthenticationController authenticatedFixture = mockControllerFactory.getAuthenticationController(true);
		AuthenticationJson response = authenticatedFixture.get();
		Assert.assertEquals(authenticatedFixture.authenticationProvider.getAuthentication().getUser().getUserId(), response.getUserId());
	}
	
}
