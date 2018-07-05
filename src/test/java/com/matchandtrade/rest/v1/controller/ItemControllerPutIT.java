package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemControllerPutIT {

	private ItemController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private UserRandom userRandom;
	

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemController(true);
		}
	}

	@Test
	public void shouldEditItem() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity existingItem = itemRandom.nextPersistedEntity(existingTradeMemberhip);
		ItemJson request = ItemTransformer.transform(existingItem);
		
		String itemName = "ItemName";
		String itemDescription = "ItemDescription";
		request.setName(itemName);
		request.setDescription(itemDescription);
		
		ItemJson response = fixture.put(existingTradeMemberhip.getTradeMembershipId(), request.getArticleId(), request);
		assertEquals(itemName, response.getName());
		assertEquals(itemDescription, response.getDescription());
	}	

	@Test(expected=RestException.class)
	public void shouldErrorWhenEditingAnInvalidArticleId() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemJson request = ItemRandom.nextJson();
		fixture.put(existingTradeMemberhip.getTradeMembershipId(), -1, request);
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenAnItemWithTheSameNameAlreadyExists() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity existingItem = itemRandom.nextPersistedEntity(existingTradeMemberhip);
		ItemEntity existingItem2 = itemRandom.nextPersistedEntity(existingTradeMemberhip);
		ItemJson request = new ItemJson();
		request.setName(existingItem.getName());
		fixture.put(existingTradeMemberhip.getTradeMembershipId(), existingItem2.getArticleId(), request);
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorTryingToEditAnItemThatNotBelongsToTheCurrentUser() {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ItemEntity existingItem = itemRandom.nextPersistedEntity(existingTradeMemberhip);
		ItemJson request = ItemTransformer.transform(existingItem);
		request.setName(request.getName() + "-Updated");
		fixture.put(existingTradeMemberhip.getTradeMembershipId(), request.getArticleId(), request);
	}
	
}
