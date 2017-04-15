package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRespository;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.validator.ValidationException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerPutIT {
	
	private UserController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRespository userRepository;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getUserController();
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
		UserEntity userEntity = userRepository.get(fixture.authenticationProvider.getAuthentication().getUserId());
		UserJson requestJson = UserRandom.nextJson();
		// Need to keep the same email as per validation rules
		requestJson.setEmail(userEntity.getEmail());
		UserJson responseJson = fixture.put(userEntity.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}

}
