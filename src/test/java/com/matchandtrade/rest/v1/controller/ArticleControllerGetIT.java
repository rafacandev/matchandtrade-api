package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerGetIT {

	private ArticleController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void get_When_ArticleIdExists_Then_ReturnExistingArticle() {
		ArticleEntity article = articleRandom.createPersistedEntity();
		ArticleJson expected = ArticleTransformer.transform(article);
		ArticleJson actual = fixture.get(expected.getArticleId());
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	public void get_When_ArticlesExists_Then_ReturnExistingArticles() {
		int startingTotal = (int) fixture.get(1, 1).getPagination().getTotal();
		ArticleEntity article = articleRandom.createPersistedEntity();
		SearchResult<ArticleJson> actual = fixture.get(1, 1);
		assertTrue(actual.getPagination().getTotal() > startingTotal);
	}

}
