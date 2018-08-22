package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerPutIT {

	private ArticleController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldEditItemData() {
		// Setup
		ItemEntity entity = itemRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		ItemJson json = ItemTransformer.transform(entity);
		// Edit data
		String name = "Edited name";
		String description = "Edited description";
		json.setName(name);
		json.setDescription(description);
		// Edit item
		ArticleJson response = fixture.put(entity.getArticleId(), json);
		// Assertions
		assertEquals(entity.getArticleId(), response.getArticleId());
		assertEquals(name, response.getName());
		assertTrue(response instanceof ItemJson);
		ItemJson responseAsItem = (ItemJson) response;
		assertEquals(description, responseAsItem.getDescription());
		assertEquals(ArticleJson.Type.ITEM, response.getType());
	}

	@Test(expected = RestException.class)
	public void shouldFailWhenEditingArticleData() {
		// Setup
		ArticleEntity entity = articleRandom.nextPersistedEntity();
		ArticleJson json = ArticleTransformer.transform(entity);
		try {			
			fixture.put(entity.getArticleId(), json);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void shouldFailWhenEditingItemThatBelongsToADifferentUser() {
		// Setup
		ItemEntity entity = itemRandom.nextPersistedEntity(userRandom.nextPersistedEntity(), false);
		ItemJson json = ItemTransformer.transform(entity);
		// Edit data
		String name = "Edited name";
		String description = "Edited description";
		json.setName(name);
		json.setDescription(description);
		// Edit item
		try {			
			fixture.put(entity.getArticleId(), json);
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

}
