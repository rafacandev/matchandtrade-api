package com.matchandtrade.trademaximizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	
//	@Test
//	@Ignore
//	public void getTrades() {
//		String results = fixture.getResults(1);
//		System.out.println("===================");
//		System.out.println(results);
//	}
	
	
	@Test
	public void post() {
		// Create a trade for a random user
		UserEntity ownerUser = userRandom.nextPersistedEntity();
		UserEntity memberUser = userRandom.nextPersistedEntity();
		TradeEntity trade = tradeRandom.nextPersistedEntity(ownerUser);
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipService.searchByTradeIdUserId(trade.getTradeId(), ownerUser.getUserId(), 1, 1);
		TradeMembershipEntity ownerTradeMembership = searchResult.getResultList().get(0);

		// Create items for trade owner (Greek letters)
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMembership);
		ItemEntity beta = itemRandom.nextPersistedEntity(ownerTradeMembership);
		
		// Create items for member s(country names)
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner wants Australia for Alpha
		WantItemJson australiaPriority1 = transform(ItemTransformer.transform(australia), 1);
		australiaPriority1 = wantItemController.post(ownerTradeMembership.getTradeMembershipId(), alpha.getItemId(), australiaPriority1);
		// Owner wants Cuba for Alpha
		WantItemJson cubaPriority2 = transform(ItemTransformer.transform(cuba), 2);
		wantItemController.post(ownerTradeMembership.getTradeMembershipId(), alpha.getItemId(), cubaPriority2);
		// Owner wants Brazil for Beta
		WantItemJson brazilPriority1 = transform(ItemTransformer.transform(brazil), 1);
		wantItemController.post(ownerTradeMembership.getTradeMembershipId(), beta.getItemId(), brazilPriority1);

		// Member wants Alpha for Australia
		WantItemJson alphaPriority1 = transform(ItemTransformer.transform(alpha), 1);
		wantItemController.post(memberTradeMemberhip.getTradeMembershipId(), australia.getItemId(), alphaPriority1);
		// Member wants Beta for Cuba
		WantItemJson betaPriority1 = transform(ItemTransformer.transform(beta), 1);
		wantItemController.post(memberTradeMemberhip.getTradeMembershipId(), cuba.getItemId(), betaPriority1);
		
		// Assertions
		assertNotNull(australiaPriority1.getWantItemId());
		assertEquals(new Integer(1), australiaPriority1.getPriority());
		
		String response = tradeController.getResults(trade.getTradeId());
		
		System.out.println(response);
		
	}

	private WantItemJson transform(ItemJson item, int priority) {
		return WantItemControllerPostIT.transform(item, priority);
	}

}
