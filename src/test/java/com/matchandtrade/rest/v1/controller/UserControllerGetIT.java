package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerGetIT {
	
	private UserController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRandom userRandom;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getUserController(true);
		}
	}
	
	@Test
	public void shouldGetUser() {
		UserJson response = fixture.get(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		assertEquals(fixture.authenticationProvider.getAuthentication().getUser().getUserId(), response.getUserId());
	}
	
	@Test
	public void shouldReturnOnlyTheUserNameFromOtherUsers() {
		UserEntity targetUser = userRandom.nextPersistedEntity("NameTest");
		assertNotNull(targetUser.getEmail());
		UserJson response = fixture.get(targetUser.getUserId());
		assertEquals("NameTest", response.getName());
		assertNull(response.getEmail());
	}

}
