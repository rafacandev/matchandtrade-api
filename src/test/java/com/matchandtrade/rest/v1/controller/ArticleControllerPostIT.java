package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerPostIT {

	private ArticleController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldCreateArticle() {
		ArticleJson expected = ArticleRandom.nextJson();
		ArticleJson actual = fixture.post(expected);
		assertNotNull(actual);
		assertNotNull(actual.getArticleId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getDescription(), actual.getDescription());
	}

}
