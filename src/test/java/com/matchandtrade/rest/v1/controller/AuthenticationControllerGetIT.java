package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.controller.MockAuthenticationControllerFactory.MockAuthenticationController;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationControllerGetIT {

	@Autowired
	AuthenticationController fixture;
	@Autowired
	MockAuthenticationControllerFactory mockAuthenticationControllerFactory;
	
	@Test(expected=AuthorizationException.class)
	public void getNegative() {
		fixture.get();
	}
	
	@Test
	public void getPositive() {
		MockAuthenticationController fixture = mockAuthenticationControllerFactory.getMockTradeController();
		AuthenticationJson response = fixture.get();
		Assert.assertEquals(fixture.authenticatedUserEntity.getUserId(), response.getUserId());
	}
	
}
