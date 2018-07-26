package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerGetIT {

	private ArticleController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldGetItem() {
		ItemEntity item = itemRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		ArticleJson response = fixture.get(item.getArticleId());
		assertEquals(item.getArticleId(), response.getArticleId());
		assertEquals(item.getName(), response.getName());
		assertTrue(response instanceof ItemJson);
		assertEquals(ArticleJson.Type.ITEM, response.getType());
	}
	
}
