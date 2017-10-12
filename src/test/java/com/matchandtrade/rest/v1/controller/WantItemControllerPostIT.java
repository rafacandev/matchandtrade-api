package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	public void basicScenario() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		ItemEntity beta = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		WantItemController memberController = mockControllerFactory.getWantItemController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner gives Alpha and wants Australia
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		// Owner gives Alpha and wants Cuba
		WantItemJson cubaPriority2 = transform(ItemTransformer.transform(cuba), 2);
		fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), cubaPriority2);
		// Member gives Beta and wants Brazil
		WantItemJson brazilPriority1 = transform(ItemTransformer.transform(brazil), 1);
		fixture.post(ownerTradeMemberhip.getTradeMembershipId(), beta.getItemId(), brazilPriority1);

		// Member gives Australia and wants Alpha
		WantItemJson alphaPriority1 = transform(ItemTransformer.transform(alpha), 1);
		fixture.post(memberTradeMemberhip.getTradeMembershipId(), australia.getItemId(), alphaPriority1);
		// Member gives Cuba and wants Beta
		WantItemJson betaPriority1 = transform(ItemTransformer.transform(beta), 1);
		betaPriority1 = fixture.post(memberTradeMemberhip.getTradeMembershipId(), cuba.getItemId(), betaPriority1);
		
		// Assertions
		assertNotNull(betaPriority1.getWantItemId());
		assertEquals(new Integer(1), betaPriority1.getPriority());
	}

	@Test(expected=RestException.class)
	public void wantItemCannotHaveDuplicatedPriority() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		WantItemController memberController = mockControllerFactory.getWantItemController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner gives Alpha and wants Australia
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		australiaPriority1 = fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		// Owner gives Alpha and wants Cuba
		WantItemJson cubaPriority2 = transform(ItemTransformer.transform(cuba), 1);
		try {
			fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), cubaPriority2);
		} catch (RestException e) {
			assertEquals("WantItem.priority must be unique within the same Item.", e.getDescription());
			throw e;
		}
	}

	@Test(expected=RestException.class)
	public void wantItemCannotBeDuplicated() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		WantItemController memberController = mockControllerFactory.getWantItemController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner gives Alpha and wants Australia
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		try {
			fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		} catch (RestException e) {
			assertEquals("WantItem.priority must be unique within the same Item.", e.getDescription());
			throw e;
		}
	}

	@Test(expected=RestException.class)
	public void wantItemMustBelongToDifferentTradeMembership() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		ItemEntity beta = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Owner gives Alpha and wants Beta
		WantItemJson betaPriority1 = transform(ItemTransformer.transform(beta), 1);
		try {
			fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), betaPriority1);
		} catch (RestException e) {
			assertEquals("WantItem.item must belong to another TradeMembership within the same Trade.", e.getDescription());
		}
	}
	
	public static WantItemJson transform(ItemJson item, Integer priority) {
		WantItemJson result = new WantItemJson();
		result.setItemId(item.getItemId());
		result.setPriority(priority);
		return result;
	}
	
}
