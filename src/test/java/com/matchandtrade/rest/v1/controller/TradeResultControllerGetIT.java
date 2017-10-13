package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

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
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.TradeMembershipService;
import com.matchandtrade.rest.v1.json.WantItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeResultControllerGetIT {
	
	private WantItemController wantItemController;
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
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;
	
	@Before
	public void before() {
		if (wantItemController == null) {
			wantItemController = mockControllerFactory.getWantItemController(false);
		}
	}
	
	@Test
	public void basicScenario() throws IOException {
		// Create a trade for a random user
		UserEntity greekUser = wantItemController.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(greekUser);
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipService.searchByTradeIdUserId(trade.getTradeId(), greekUser.getUserId(), 1, 1);
		
		// Create items for Greek letters
		TradeMembershipEntity greekTradeMembership = searchResult.getResultList().get(0);
		ItemEntity alpha = itemRandom.nextPersistedEntity(greekTradeMembership);
		ItemEntity beta = itemRandom.nextPersistedEntity(greekTradeMembership);
		
		// Create items for country names
		UserEntity countryUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity countryTradeMembership = tradeMembershipRandom.nextPersistedEntity(trade, countryUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(countryTradeMembership);
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryTradeMembership);
		ItemEntity cuba = itemRandom.nextPersistedEntity(countryTradeMembership);

		// Create items for ordinal numbers
		UserEntity ordinalUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity ordinalTradeMembership = tradeMembershipRandom.nextPersistedEntity(trade, ordinalUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity first = itemRandom.nextPersistedEntity(ordinalTradeMembership);
		ItemEntity second = itemRandom.nextPersistedEntity(ordinalTradeMembership);
		// Create a "third" ItemEntity which is never used but I added it to see if it won't be accounted in the trade
		itemRandom.nextPersistedEntity(ordinalTradeMembership);
		// Create a "fourth" ItemEntity which is never used but I added it to see if it won't be accounted in the trade
		itemRandom.nextPersistedEntity(ordinalTradeMembership);

		// Offering Alpha for Australia
		WantItemJson alphaForAustralia = WantItemControllerPostIT.transform(ItemTransformer.transform(australia), 1);
		alphaForAustralia = wantItemController.post(greekTradeMembership.getTradeMembershipId(), alpha.getItemId(), alphaForAustralia);
		// Offering Beta for Brazil
		WantItemJson betaForBrazil = WantItemControllerPostIT.transform(ItemTransformer.transform(brazil), 1);
		betaForBrazil = wantItemController.post(greekTradeMembership.getTradeMembershipId(), beta.getItemId(), betaForBrazil);
		// Offering Beta for Cuba
		WantItemJson betaForCuba = WantItemControllerPostIT.transform(ItemTransformer.transform(cuba), 2);
		betaForCuba = wantItemController.post(greekTradeMembership.getTradeMembershipId(), beta.getItemId(), betaForCuba);
		// Offering Australia for Alpha 
		WantItemJson australiaForAlpha = WantItemControllerPostIT.transform(ItemTransformer.transform(alpha), 1);
		australiaForAlpha = wantItemController.post(countryTradeMembership.getTradeMembershipId(), australia.getItemId(), australiaForAlpha);
		// Offering Brazil for First 
		WantItemJson brazilForFirst = WantItemControllerPostIT.transform(ItemTransformer.transform(first), 1);
		brazilForFirst = wantItemController.post(countryTradeMembership.getTradeMembershipId(), brazil.getItemId(), brazilForFirst);
		// Offering First for Brazil 
		WantItemJson firstForBrazil = WantItemControllerPostIT.transform(ItemTransformer.transform(brazil), 1);
		firstForBrazil = wantItemController.post(ordinalTradeMembership.getTradeMembershipId(), first.getItemId(), firstForBrazil);
		// Offering Second for Brazil
		WantItemJson secondForBrazil = WantItemControllerPostIT.transform(ItemTransformer.transform(brazil), 1);
		secondForBrazil = wantItemController.post(ordinalTradeMembership.getTradeMembershipId(), second.getItemId(), secondForBrazil);
		
		// Generate the trade results
		trade.setState(TradeEntity.State.MATCHING_ITEMS_ENDED);
		tradeRepositoryFacade.save(trade);
		TradeResultController tradeResultController = mockControllerFactory.getTradeResultController(true);
		String response = tradeResultController.getResults(trade.getTradeId());
		String[] responseLines = response.split("\n");

		// Assert header
		assertEquals("offering_user_id,offering_user_name,offering_item_id,offering_item_name,receiving_user_id,receiving_user_name,receiving_item_id,receiving_item_name",
				responseLines[0]);

		// Offering Alpha for Australia
		assertEquals(responseLines[1], buildCsvLine(greekTradeMembership, alpha, countryTradeMembership, australia));
		// Offering Australia for Alpha
		assertEquals(responseLines[2], buildCsvLine(countryTradeMembership, australia, greekTradeMembership, alpha));
		// Offering Brazil for First 
		assertEquals(responseLines[3], buildCsvLine(countryTradeMembership, brazil, ordinalTradeMembership, first));
		// Offering Brazil for First 
		assertEquals(responseLines[4], buildCsvLine(ordinalTradeMembership, first, countryTradeMembership, brazil));
		
		// Assert summary
		assertTrue(responseLines[7].contains(trade.getName()) );
		assertTrue(responseLines[8].contains("9") );
		assertTrue(responseLines[9].contains("4") );
		assertTrue(responseLines[10].contains("5") );
	}

	@Test(expected = RestException.class)
	public void resultsAreOnlyGeneratedIfTradeStatusIsMatchingItemsEndedOrGeneratingTradesEnded() throws IOException {
		UserEntity greekUser = wantItemController.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(greekUser);
		// Generate the trade results
		TradeResultController tradeResultController = mockControllerFactory.getTradeResultController(true);
		try {
			tradeResultController.getResults(trade.getTradeId());
		} catch (RestException e) {
			assertEquals("TradeResult is only availble when Trade.State is MATCHING_ITEMS_ENDED or GENERATING_TRADES_ENDED", e.getDescription());
			throw e;
		}
		
	}

	private String buildCsvLine(
			TradeMembershipEntity greekTradeMembership,
			ItemEntity alpha,
			TradeMembershipEntity countryTradeMembership,
			ItemEntity australia) {
		String alphaForAustraliaString = greekTradeMembership.getUser().getUserId() + ","
				+ greekTradeMembership.getUser().getName() + ","
				+ alpha.getItemId() + ","
				+ alpha.getName() + ","
				+ countryTradeMembership.getUser().getUserId() + ","
				+ countryTradeMembership.getUser().getName() + ","
				+ australia.getItemId() + ","
				+ australia.getName();
		return alphaForAustraliaString;
	}
}
