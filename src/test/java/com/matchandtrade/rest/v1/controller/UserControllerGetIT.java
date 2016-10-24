package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.IntegrationTestStore;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerGetIT {
	
	@Autowired
	private MockFactory mockFactory;
	private UserAuthentication userAuthentication;
	@Autowired
	private UserController userController;
	
	@Before
	public void beforeClass() {
		userAuthentication = mockFactory.getUserAuthentication();
		IntegrationTestStore.add(IntegrationTestStore.StoredObject.UserAuthentication, userAuthentication);
	}
	
	@Test(expected=AuthorizationException.class)
	public void getNegative() {
		// the database is not supposed to hold negative userId. Therefore is a safe assumption to say that it will throw AuthorizationException
		userController.get(-1);
	}
	
	@Test
	@Rollback(false)
	public void getPositive() {
		MockHttpServletRequest httpRequest = mockFactory.getHttpRquestWithAuthenticatedUserFromIntegrationTestStore();
		userController.setHttpServletRequest(httpRequest);
		UserJson response = userController.get(userAuthentication.getUserId());
		assertEquals(userAuthentication.getUserId(), response.getUserId());
	}

}
