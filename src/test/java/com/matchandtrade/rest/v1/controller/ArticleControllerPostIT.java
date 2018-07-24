package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerPostIT {

	private ArticleController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldCreateArticle() {
		ItemJson request = ItemRandom.nextJson();
		request.setName("ArticleControllerPostIT.shouldCreateName.name");
		request.setDescription("ArticleControllerPostIT.shouldCreateName.description");
		ArticleJson response = fixture.post(request);
		assertNotNull(response.getArticleId());
		assertEquals(request.getName(), response.getName());

		assertTrue(request instanceof ItemJson);
		ItemJson responseAsItem = (ItemJson) response;
		assertEquals(request.getDescription(), responseAsItem.getDescription());
	}
//	
//	@Test(expected = RestException.class)
//	public void postCannotHaveDuplicatedName() {
//		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
//		ItemJson item = ItemRandom.nextJson();
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//	}
//	
//	@Test(expected=RestException.class)
//	public void postNameIsNull() {
//		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
//		ItemJson item = ItemRandom.nextJson();
//		item.setName(null);
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//	}
//
//	@Test(expected = RestException.class)
//	public void postNameTooLong() {
//		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
//		ItemJson item = ItemRandom.nextJson();
//		item.setName("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//	}
//
//	@Test(expected = RestException.class)
//	public void shouldNotAllowDescriptionGreaterThan500Characters() {
//		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
//		ItemJson item = ItemRandom.nextJson();
//		item.setName("12345678901234567890");
//		item.setDescription("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
//				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
//				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
//				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
//				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
//				+ "500 Characters above.");
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//	}
//	
//	@Test(expected = RestException.class)
//	public void postNameTooShort() {
//		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
//		ItemJson item = ItemRandom.nextJson();
//		item.setName("ab");
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//	}
//
//	@Test(expected = RestException.class)
//	public void postTradeMembershipNotFound() {
//		ItemJson item = ItemRandom.nextJson();
//		fixture.post(-1, item);
//	}
//
//	@Test(expected=RestException.class)
//	public void postUserNotAssociatedWithTradeMembership() {
//		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
//		ItemJson item = ItemRandom.nextJson();
//		fixture = mockControllerFactory.getItemController(false);
//		fixture.post(existingTradeMemberhip.getTradeMembershipId(), item);
//	}
	
}
