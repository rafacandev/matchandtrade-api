package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

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
	public void shouldCreateItem() {
		ItemJson request = new ItemJson();
		request.setName("ArticleControllerPostIT.shouldCreateName.name");
		request.setDescription("ArticleControllerPostIT.shouldCreateName.description");
		ArticleJson response = fixture.post(request);
		assertNotNull(response.getArticleId());
		assertEquals(request.getName(), response.getName());
		assertTrue(request instanceof ItemJson);
		ItemJson responseAsItem = (ItemJson) response;
		assertEquals(request.getDescription(), responseAsItem.getDescription());
	}
	
	@Test(expected = RestException.class)
	public void shouldErrorWhenArticleIsNotItem() {
		ArticleJson request = new ArticleJson();
		try {
			fixture.post(request);
		} catch (RestException e) {
			assertEquals("Article must be of type Item.", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void shouldErrorWhenArticleNameIsLonggerThan150Character() {
		ItemJson request = new ItemJson();
		request.setName("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
		try {
			fixture.post(request);
		} catch (RestException e) {
			assertEquals("Article name must be equal or less than 150 characters long.", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void shouldErrorWhenArticleNameIsNull() {
		ItemJson request = new ItemJson();
		try {
			fixture.post(request);
		} catch (RestException e) {
			assertEquals("Article name is mandatory.", e.getDescription());
			throw e;
		}
	}
	
	@Test(expected = RestException.class)
	public void shouldErrorWhenArticleNameIsSmallerThan3Character() {
		ItemJson request = new ItemJson();
		request.setName("ab");
		try {
			fixture.post(request);
		} catch (RestException e) {
			assertEquals("Article name must be at least 3 characters long.", e.getDescription());
			throw e;
		}
	}
	
	@Test(expected = RestException.class)
	public void shouldErrorWhenDescriptionIsLongerThan500Characters() {
		ItemJson request = new ItemJson();
		request.setName("ArticleControllerPostIT.name");
		StringBuilder description = new StringBuilder();
		description.append("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		description.append("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		description.append("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		description.append("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		description.append("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		description.append("0");
		request.setDescription(description.toString());
		try {
			fixture.post(request);
		} catch (RestException e) {
			assertEquals("Item.description must be equal or less than 500 characters long.", e.getDescription());
			throw e;
		}
	}
	

}
