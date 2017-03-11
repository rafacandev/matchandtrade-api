package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.v1.controller.MockUserControllerFactory.MockUserController;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerSearchIT {
	
	@Autowired
	private MockUserControllerFactory mockUserControllerFactory;
	private MockUserController fixture;

	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockUserControllerFactory.getMockUserController();
		}
	}
	
	@Test
	public void searchPositive() {
		SearchResult<UserJson> response = fixture.get(0, 5, fixture.userEntity.getEmail());
		UserJson responseContent = response.getResultList().get(0);
		Assert.assertEquals(fixture.userEntity.getEmail(), responseContent.getEmail());
	}
	
	@Test
	public void searchNegative() {
		SearchResult<UserJson> response = fixture.get(0, 5, StringRandom.nextString());
		Assert.assertTrue(response.getResultList().isEmpty());
	}

}
