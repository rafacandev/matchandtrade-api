package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.AuthenticationResponseJson;
import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationControllerGetIT {

	@Autowired
	private AuthenticationController authenticationController;
	@Autowired
	private MockFactory mockFactory;
	
	@Test(expected=AuthorizationException.class)
	public void getNegative() {
		MockHttpServletRequest httpRequest = new MockHttpServletRequest();
		authenticationController.setHttpServletRequest(httpRequest);
		AuthenticationJson response = authenticationController.get();
		Assert.assertNull(response);
	}
	
	@Test
	public void getPositive() {
		AuthenticationResponseJson userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		MockHttpServletRequest httpRequest = mockFactory.getHttpRequestWithAuthenticatedUser(userAuthentication);
		authenticationController.setHttpServletRequest(httpRequest);
		AuthenticationJson response = authenticationController.get();
		Assert.assertEquals(userAuthentication.getUserId(), response.getUserId());
	}
	
}
