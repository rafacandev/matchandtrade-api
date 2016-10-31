package com.matchandtrade.rest.v1.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.UserDao;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class UserControllerSearchIT {
	
	@Autowired
	UserDao userDao;
	@Autowired
	UserController userController;
	
	private UserEntity userEntity;
	
	@Before
	@Commit
	public void beforeClass() {
		userEntity = new UserEntity();
		userEntity.setEmail(StringRandom.nextEmail());
		userEntity.setName(StringRandom.nextName());
		userDao.save(userEntity);
	}
	
	@Test
	@Commit
	public void search() {
		SearchResult<UserJson> response = userController.getBySearching(0, 5, userEntity.getEmail());
		UserJson responseContent = response.getResultList().get(0);
		Assert.assertEquals(userEntity.getEmail(), responseContent.getEmail());
	}

}
