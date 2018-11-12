package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.UserRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerDeleteIT {

	private ArticleController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void delete_When_ArticleIdExists_Expects_Success() {
		UserEntity user = fixture.authenticationProvider.getAuthentication().getUser();
		ArticleEntity actual = new ArticleEntity();
		actual.setName("actual");
		articleRepositoryFacade.save(actual);
		user.getArticles().add(actual);
		userRepositoryFacade.save(user);
		fixture.delete(actual.getArticleId());
	}

	@Test(expected = RestException.class)
	public void delete_When_ArticleIdBelongsToDifferentUser_Expects_BadRequest() {
		UserEntity user = userRandom.createPersistedEntity();
		ArticleEntity actual = new ArticleEntity();
		actual.setName("actual");
		articleRepositoryFacade.save(actual);
		user.getArticles().add(actual);
		userRepositoryFacade.save(user);
		try {
			fixture.delete(actual.getArticleId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
