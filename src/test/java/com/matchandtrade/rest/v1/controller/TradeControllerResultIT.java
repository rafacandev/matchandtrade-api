package com.matchandtrade.rest.v1.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.TradeMembershipService;
import com.matchandtrade.rest.v1.controller.MockControllerFactory;
import com.matchandtrade.rest.v1.controller.TradeController;
import com.matchandtrade.rest.v1.controller.WantItemController;
import com.matchandtrade.rest.v1.controller.WantItemControllerPostIT;
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
public class TradeControllerResultIT {
	
	private WantItemController greekController;
	private WantItemController countryController;
	private WantItemController ordinalController;
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
	@Autowired
	private TradeMembershipService tradeMembershipService;
	
	@Before
	public void before() {
		if (greekController == null) {
			greekController = mockControllerFactory.getWantItemController(false);
		}
		if (countryController == null) {
			countryController = mockControllerFactory.getWantItemController(false);
		}
		if (ordinalController == null) {
			ordinalController = mockControllerFactory.getWantItemController(false);
		}
	}
	
	@Test
	public void post() {
		// Create a trade for a random user
		UserEntity greekUser = greekController.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(greekUser);
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipService.searchByTradeIdUserId(trade.getTradeId(), greekUser.getUserId(), 1, 1);
		
		// Create items for Greek letters
		TradeMembershipEntity greekTradeMembership = searchResult.getResultList().get(0);
		ItemEntity alpha = itemRandom.nextPersistedEntity(greekTradeMembership);
		ItemEntity beta = itemRandom.nextPersistedEntity(greekTradeMembership);
		
		// Create items for country names
		UserEntity countryUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity countryTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, countryUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(countryTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryTradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(countryTradeMemberhip);

		// Create items for ordinal numbers
		UserEntity ordinalUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity ordinalTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, ordinalUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity first = itemRandom.nextPersistedEntity(ordinalTradeMemberhip);
		ItemEntity second = itemRandom.nextPersistedEntity(ordinalTradeMemberhip);
		// Third is never used, but we add it just to see trademaximizer's response
		ItemEntity third = itemRandom.nextPersistedEntity(ordinalTradeMemberhip);

		// Alpha for Australia
		WantItemJson alphaForAustralia = transform(ItemTransformer.transform(australia), 1);
		alphaForAustralia = greekController.post(greekTradeMembership.getTradeMembershipId(), alpha.getItemId(), alphaForAustralia);
		// Beta for Brazil
		WantItemJson betaForBrazil = transform(ItemTransformer.transform(brazil), 1);
		betaForBrazil = greekController.post(greekTradeMembership.getTradeMembershipId(), beta.getItemId(), betaForBrazil);
		// Beta for Cuba
		WantItemJson betaForCuba = transform(ItemTransformer.transform(cuba), 2);
		betaForCuba = greekController.post(greekTradeMembership.getTradeMembershipId(), beta.getItemId(), betaForCuba);
		// Australia for Alpha 
		WantItemJson australiaForAlpha = transform(ItemTransformer.transform(alpha), 1);
		australiaForAlpha = countryController.post(countryTradeMemberhip.getTradeMembershipId(), australia.getItemId(), australiaForAlpha);
		// Brazil for First 
		WantItemJson brazilForFirst = transform(ItemTransformer.transform(first), 1);
		brazilForFirst = countryController.post(countryTradeMemberhip.getTradeMembershipId(), brazil.getItemId(), brazilForFirst);
		// First for Brazil 
		WantItemJson firstForBrazil = transform(ItemTransformer.transform(brazil), 1);
		firstForBrazil = ordinalController.post(ordinalTradeMemberhip.getTradeMembershipId(), first.getItemId(), firstForBrazil);
		// Second for Brazil
		WantItemJson secondForBrazil = transform(ItemTransformer.transform(brazil), 1);
		secondForBrazil = ordinalController.post(ordinalTradeMemberhip.getTradeMembershipId(), second.getItemId(), secondForBrazil);
		
		TradeController tradeController = mockControllerFactory.getTradeController(true);
		System.out.println(tradeController.authenticationProvider.getAuthentication());
		String response = tradeController.getResults(trade.getTradeId());
		
		System.out.println(response);
		
	}

	private WantItemJson transform(ItemJson item, int priority) {
		return WantItemControllerPostIT.transform(item, priority);
	}

}
