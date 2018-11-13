package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerPutIT {

	private ArticleController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;


	@Autowired
	private UserRepositoryFacade userRepositoryFacade;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController();
		}
	}

	@Test
	@Transactional
	public void put_When_ArticleIdExists_Expects_Success() {
		ArticleEntity articleEntity = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson expected = ArticleTransformer.transform(articleEntity);
		ArticleJson actual = fixture.put(expected.getArticleId(), expected);
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test(expected = RestException.class)
	public void put_When_ArticleBelongsToDifferentUser_Expects_BadRequest() {
		ArticleEntity articleEntity = articleRandom.createPersistedEntity();
		ArticleJson expected = ArticleTransformer.transform(articleEntity);
		try {
			fixture.put(expected.getArticleId(), expected);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
