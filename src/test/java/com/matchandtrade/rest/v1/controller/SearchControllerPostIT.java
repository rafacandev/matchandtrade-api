package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class SearchControllerPostIT {
	
	private SearchController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getSearchController(true);
		}
	}
	
	@Test
	public void postBasicSearch() {
		// Create a trade for a random user
		UserEntity greekUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity greekTradeMembership = tradeMembershipRandom.nextPersistedEntity(greekUser);
		TradeEntity trade = greekTradeMembership.getTrade();

		// Create items for Greek letters
		ItemEntity alpha = itemRandom.nextPersistedEntity(greekTradeMembership);
		ItemEntity beta = itemRandom.nextPersistedEntity(greekTradeMembership);

		// Create items for country names
		UserEntity countryUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity countryTradeMembership = tradeMembershipRandom.nextPersistedEntity(trade, countryUser, TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(countryTradeMembership);
		ItemEntity brazil = itemRandom.nextPersistedEntity(countryTradeMembership);
		ItemEntity cuba = itemRandom.nextPersistedEntity(countryTradeMembership);
		
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe(Recipe.ITEMS);
		request.addCriterion("trade.tradeId", trade.getTradeId());

		SearchResult<Json> response = fixture.post(request, 1, 10);
		assertEquals(5, response.getResultList().size());
		assertTrue(containsItem(response, alpha));
		assertTrue(containsItem(response, beta));
		assertTrue(containsItem(response, australia));
		assertTrue(containsItem(response, brazil));
		assertTrue(containsItem(response, cuba));
	}

	private boolean containsItem(SearchResult<Json> response, ItemEntity targetItem) {
		return response.getResultList().stream().map(i -> {
			ItemJson responseItem = (ItemJson) i;
			return responseItem;
		})
		.anyMatch(v -> v.getName().equals(targetItem.getName()));
	}

}
