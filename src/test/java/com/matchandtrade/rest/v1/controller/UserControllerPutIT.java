package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerPutIT {
	
	private UserController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getUserController(false);
		}
	}
	
	@Test
	public void put() {
		UserEntity authenticatedUser = fixture.authenticationProvider.getAuthentication().getUser();
		UserJson requestJson = UserTransformer.transform(authenticatedUser);
		requestJson.setName(requestJson.getName() + " - User.name after PUT");
		UserJson responseJson = fixture.put(authenticatedUser.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=RestException.class)
	public void putUnauthorized() {
		fixture.put(-1, new UserJson());
	}	

}
