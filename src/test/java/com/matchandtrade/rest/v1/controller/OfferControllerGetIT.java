package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
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

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getOfferController(true);
		}
	}
	
	@Test
	public void shouldGetByOfferId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australia.getItemId());

		OfferJson response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), alphaForAustralia.getOfferId());
		assertEquals(alphaForAustralia.getOfferId(), response.getOfferId());
		assertEquals(alphaForAustralia.getOfferedItem().getItemId(), response.getOfferedItemId());
		assertEquals(alphaForAustralia.getWantedItem().getItemId(), response.getWantedItemId());
	}

	@Test
	public void shouldGetAll() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity canada = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australia.getItemId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), brazil.getItemId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), canada.getItemId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), null, null, 1, 10);
		assertEquals(3, response.getPagination().getTotal());
		assertEquals(alphaForAustralia.getOfferId(), response.getResultList().get(0).getOfferId());
	}
	
	@Test
	public void shouldGetByOfferedItemId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		ItemEntity beta = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity canada = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), australia.getItemId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), brazil.getItemId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), beta.getItemId(), canada.getItemId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), alpha.getItemId(), null, 1, 10);
		assertEquals(2, response.getPagination().getTotal());
	}


}
