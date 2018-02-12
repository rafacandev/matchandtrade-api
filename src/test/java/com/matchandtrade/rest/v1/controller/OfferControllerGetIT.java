package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerGetIT {
	
	private OfferController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getOfferController(true);
		}
	}
	
	@Test
	public void shouldGetByOfferId() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(alpha.getItemId(), australia.getItemId());

		OfferJson response = fixture.get(alphaForAustralia.getOfferId());
		assertEquals(alphaForAustralia.getOfferId(), response.getOfferId());
		assertEquals(alphaForAustralia.getOfferedItem().getItemId(), response.getOfferedItemId());
		assertEquals(alphaForAustralia.getWantedItem().getItemId(), response.getWantedItemId());
	}

}
