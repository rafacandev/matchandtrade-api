package com.matchandtrade.rest.v1.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.json.WantItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class WantItemControllerPostIT {

	private WantItemController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getWantItemController(false);
		}
	}

	@Test
	public void post() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create items for user1 (Greek letters)
		TradeMembershipEntity user1TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity alpha = itemRandom.nextPersistedEntity(user1TradeMemberhip);
		ItemEntity beta = itemRandom.nextPersistedEntity(user1TradeMemberhip);
		
		// Create items for user2 (country names)
		WantItemController controllerAuthenticatedWithUser2 = mockControllerFactory.getWantItemController(false);
		TradeMembershipEntity user2TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, controllerAuthenticatedWithUser2.authenticationProvider.getAuthentication().getUser());
		ItemEntity australia = itemRandom.nextPersistedEntity(user2TradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(user2TradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(user2TradeMemberhip);

		// User1 wants Australia for Alpha
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		// User1 wants Cuba for Alpha
		WantItemJson cubaPriority2 = transform(ItemTransformer.transform(cuba), 2);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), cubaPriority2);
		// User1 wants Brazil for Beta
		WantItemJson brazilPriority1 = transform(ItemTransformer.transform(brazil), 1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), beta.getItemId(), brazilPriority1);

		// User2 wants Alpha for Australia
		WantItemJson alphaPriority1 = transform(ItemTransformer.transform(alpha), 1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), australia.getItemId(), alphaPriority1);
		// User2 wants Beta for Cuba
		WantItemJson betaPriority1 = transform(ItemTransformer.transform(beta), 1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), cuba.getItemId(), betaPriority1);
	}

	@Test(expected=RestException.class)
	public void postWantItemCannotHaveDuplicatedPriority() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create items for user1 (Greek letters)
		TradeMembershipEntity user1TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity alpha = itemRandom.nextPersistedEntity(user1TradeMemberhip);
		
		// Create items for user2 (country names)
		WantItemController controllerAuthenticatedWithUser2 = mockControllerFactory.getWantItemController(false);
		TradeMembershipEntity user2TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, controllerAuthenticatedWithUser2.authenticationProvider.getAuthentication().getUser());
		ItemEntity australia = itemRandom.nextPersistedEntity(user2TradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(user2TradeMemberhip);
		
		// User1 wants Australia for Alpha
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		// User1 wants Cuba for Alpha
		WantItemJson cubaPriority2 = transform(ItemTransformer.transform(cuba), 1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), cubaPriority2);
	}

	@Test(expected=RestException.class)
	public void postWantItemCannotBeDuplicated() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create items for user1 (Greek letters)
		TradeMembershipEntity user1TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity alpha = itemRandom.nextPersistedEntity(user1TradeMemberhip);
		
		// Create items for user2 (country names)
		WantItemController controllerAuthenticatedWithUser2 = mockControllerFactory.getWantItemController(false);
		TradeMembershipEntity user2TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, controllerAuthenticatedWithUser2.authenticationProvider.getAuthentication().getUser());
		ItemEntity australia = itemRandom.nextPersistedEntity(user2TradeMemberhip);
		
		// User1 wants Australia for Alpha
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		WantItemJson australiaPriority2 = transform(ItemTransformer.transform(australia), 2);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		fixture.post(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority2);
	}
	
	public static WantItemJson transform(ItemJson item, Integer priority) {
		WantItemJson result = new WantItemJson();
		result.setItem(item);
		result.setPriority(priority);
		return result;
	}
	
}
