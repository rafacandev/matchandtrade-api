package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.rest.v1.json.WantItemJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class WantItemControllerGetIT {

	private WantItemController fixture;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
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
			fixture = mockControllerFactory.getWantItemController(true);
		}
	}

	@Test
	public void get() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		// Create items for user1 (Greek letters)
		TradeMembershipEntity user1TradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(user1TradeMemberhip);
		// Create items for user2 (country names)
		ItemEntity australia = itemRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		// User1 wants Australia for Alpha
		WantItemEntity wantsAustraliaForAlpha = new WantItemEntity();
		wantsAustraliaForAlpha.setItem(australia);
		wantsAustraliaForAlpha.setPriority(1);
		alpha.getWantItems().add(wantsAustraliaForAlpha);
		itemRepositoryFacade.save(alpha);
		SearchResult<WantItemJson> response = fixture.get(user1TradeMemberhip.getTradeMembershipId(), alpha.getItemId(), 1, 1);
		assertEquals(1, response.getResultList().size());
	}
	
}
