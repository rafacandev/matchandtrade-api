package com.matchandtrade.trademaximizer;

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
public class TradeMaximizerIT {
	
	private WantItemController wantItemController;
	private TradeController tradeController;
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
		if (wantItemController == null) {
			wantItemController = mockControllerFactory.getWantItemController(true);
			tradeController = mockControllerFactory.getTradeController(true);
		}
	}
	
	@Test
	public void post() {
		// Create a trade for a random user
		UserEntity ownerUser = userRandom.nextPersistedEntity();
		UserEntity memberUser = userRandom.nextPersistedEntity();
		TradeEntity trade = tradeRandom.nextPersistedEntity(ownerUser);
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipService.searchByTradeIdUserId(trade.getTradeId(), ownerUser.getUserId(), 1, 1);
		
		// Create items for Greek letters
		TradeMembershipEntity greekTradeMembership = searchResult.getResultList().get(0);
		ItemEntity alpha = itemRandom.nextPersistedEntity(greekTradeMembership);
		ItemEntity beta = itemRandom.nextPersistedEntity(greekTradeMembership);
		
		// Create items for country names
		TradeMembershipEntity countryTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(countryTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryTradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(countryTradeMemberhip);

		// Create items for ordinal numbers
		TradeMembershipEntity ordinalTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity first = itemRandom.nextPersistedEntity(ordinalTradeMemberhip);
		ItemEntity second = itemRandom.nextPersistedEntity(ordinalTradeMemberhip);
		// Third is never used, but we add it just to see trademaximizer's response
		ItemEntity third = itemRandom.nextPersistedEntity(ordinalTradeMemberhip);

		// Alpha for Australia
		WantItemJson alphaForAustralia = transform(ItemTransformer.transform(australia), 1);
		alphaForAustralia = wantItemController.post(greekTradeMembership.getTradeMembershipId(), alpha.getItemId(), alphaForAustralia);
		// Beta for Brazil
		WantItemJson betaForBrazil = transform(ItemTransformer.transform(brazil), 1);
		betaForBrazil = wantItemController.post(greekTradeMembership.getTradeMembershipId(), beta.getItemId(), betaForBrazil);
		// Beta for Cuba
		WantItemJson betaForCuba = transform(ItemTransformer.transform(cuba), 2);
		betaForCuba = wantItemController.post(greekTradeMembership.getTradeMembershipId(), beta.getItemId(), betaForCuba);
		// Australia for Alpha 
		WantItemJson australiaForAlpha = transform(ItemTransformer.transform(alpha), 1);
		australiaForAlpha = wantItemController.post(countryTradeMemberhip.getTradeMembershipId(), australia.getItemId(), australiaForAlpha);
		// Brazil for First 
		WantItemJson brazilForFirst = transform(ItemTransformer.transform(first), 1);
		brazilForFirst = wantItemController.post(countryTradeMemberhip.getTradeMembershipId(), brazil.getItemId(), brazilForFirst);
		// First for Brazil 
		WantItemJson firstForBrazil = transform(ItemTransformer.transform(brazil), 1);
		firstForBrazil = wantItemController.post(ordinalTradeMemberhip.getTradeMembershipId(), first.getItemId(), firstForBrazil);
		// Second for Brazil 
		WantItemJson secondForBrazil = transform(ItemTransformer.transform(brazil), 1);
		secondForBrazil = wantItemController.post(ordinalTradeMemberhip.getTradeMembershipId(), second.getItemId(), secondForBrazil);
		
		String response = tradeController.getResults(trade.getTradeId());
		
		System.out.println(response);
		
	}

	private WantItemJson transform(ItemJson item, int priority) {
		return WantItemControllerPostIT.transform(item, priority);
	}

}
