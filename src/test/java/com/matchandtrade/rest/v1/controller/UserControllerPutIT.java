package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerPutIT {
	
	private UserController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRepository userRepository;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getUserController(true);
		}
	}
	
	@Test
	public void put() {
		UserEntity userEntity = userRepository.get(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		UserJson requestJson = UserRandom.nextJson();
		// Need to keep the same email as per validation rules
		requestJson.setEmail(userEntity.getEmail());
		UserJson responseJson = fixture.put(userEntity.getUserId(), requestJson);
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=RestException.class)
	public void putUnauthorized() {
		fixture.put(-1, new UserJson());
	}	

}
