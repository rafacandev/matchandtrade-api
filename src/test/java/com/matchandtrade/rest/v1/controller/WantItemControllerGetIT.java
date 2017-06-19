package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.matchandtrade.persistence.repository.WantItemRepository;
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
	private WantItemRepository wantItemRepository;
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

	private void buildTradeMemembershipAndItemEntity(TradeMembershipEntity tradeMembershipToBuild, ItemEntity itemToBuild, WantItemEntity wantItemToBuild) {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		// Create items for a user (Greek letters)
		TradeMembershipEntity tradeMembership = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity alpha = itemRandom.nextPersistedEntity(tradeMembership);
		// Create items for another user (country names)
		ItemEntity australia = itemRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		// User1 wants Australia for Alpha
		wantItemToBuild.setItem(australia);
		wantItemToBuild.setPriority(1);
		wantItemRepository.save(wantItemToBuild);
		alpha.getWantItems().add(wantItemToBuild);
		itemRepositoryFacade.save(alpha);
		tradeMembershipToBuild.setTradeMembershipId(tradeMembership.getTradeMembershipId());
		itemToBuild.setItemId(alpha.getItemId());
	}

	@Test
	public void getAll() {
		TradeMembershipEntity tradeMembership = new TradeMembershipEntity();
		ItemEntity item = new ItemEntity();
		buildTradeMemembershipAndItemEntity(tradeMembership, item, new WantItemEntity());
		SearchResult<WantItemJson> response = fixture.get(tradeMembership.getTradeMembershipId(), item.getItemId(), 1, 1);
		assertEquals(1, response.getResultList().size());
	}

	@Test
	public void getOne() {
		TradeMembershipEntity tradeMembership = new TradeMembershipEntity();
		ItemEntity item = new ItemEntity();
		WantItemEntity wantItem = new WantItemEntity();
		buildTradeMemembershipAndItemEntity(tradeMembership, item, wantItem);
		
		// Asserts the response
		WantItemJson response = fixture.get(tradeMembership.getTradeMembershipId(), item.getItemId(), wantItem.getWantItemId());
		assertEquals(wantItem.getWantItemId(), response.getWantItemId());
		assertEquals(wantItem.getItem().getItemId(), response.getItemId());
		assertEquals(wantItem.getPriority(), response.getPriority());
		
		// Assert invalid scenarios
		WantItemJson invalidTradeMembershipId = fixture.get(-1, item.getItemId(), wantItem.getWantItemId());
		assertNull(invalidTradeMembershipId);
		WantItemJson invalidItemId = fixture.get(tradeMembership.getTradeMembershipId(), -1, wantItem.getWantItemId());
		assertNull(invalidItemId);
		WantItemJson invalidWantItemId = fixture.get(tradeMembership.getTradeMembershipId(), item.getItemId(), -1);
		assertNull(invalidWantItemId);
	}

}
