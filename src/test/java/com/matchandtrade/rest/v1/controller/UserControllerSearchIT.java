package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerSearchIT {
	
	@Autowired
	private UserModel userModel;
	@Autowired
	private UserController userController;
	@Autowired
	private MockFactory mockFactory;
	private UserEntity userEntity;
	
	@Before
	@Commit
	public void before() {
		UserAuthentication userAuthentication = mockFactory.nextRandomUserAuthentication();
		MockHttpServletRequest httpRequest = mockFactory.getHttpRquestWithAuthenticatedUser(userAuthentication);
		userController.setHttpServletRequest(httpRequest);
		userEntity = userModel.get(userAuthentication.getUserId());
	}
	
	@Test
	public void searchPositive() {
		SearchResult<UserJson> response = userController.searching(0, 5, userEntity.getEmail());
		UserJson responseContent = response.getResultList().get(0);
		Assert.assertEquals(userEntity.getEmail(), responseContent.getEmail());
	}
	
	@Test
	public void searchNegative() {
		SearchResult<UserJson> response = userController.searching(0, 5, StringRandom.nextString());
		Assert.assertTrue(response.getResultList().isEmpty());
	}

}
