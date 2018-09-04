package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.json.search.Matcher;
import com.matchandtrade.rest.v1.json.search.Operator;
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
	public void searchByTradeId() {
		// Create a trade for a random user
		UserEntity greekUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity greekTradeMembership = tradeMembershipRandom.nextPersistedEntity(greekUser);
		TradeEntity trade = greekTradeMembership.getTrade();

		// Create items for Greek letters
		ArticleEntity alpha = itemRandom.nextPersistedEntity(greekTradeMembership);
		ArticleEntity beta = itemRandom.nextPersistedEntity(greekTradeMembership);

		// Create items for country names
		UserEntity countryUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity countryTradeMembership = tradeMembershipRandom.nextPersistedEntity(trade, countryUser, TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(countryTradeMembership);
		ArticleEntity brazil = itemRandom.nextPersistedEntity(countryTradeMembership);
		ArticleEntity cuba = itemRandom.nextPersistedEntity(countryTradeMembership);
		
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe(Recipe.ITEMS);
		request.addCriterion("Trade.tradeId", trade.getTradeId());

		SearchResult<Json> response = fixture.post(request, 1, 10);
		assertEquals(5, response.getResultList().size());
		assertTrue(containsItem(response, alpha));
		assertTrue(containsItem(response, beta));
		assertTrue(containsItem(response, australia));
		assertTrue(containsItem(response, brazil));
		assertTrue(containsItem(response, cuba));
	}

	@Test
	public void searchByTradeIdAndTradeMembershipIdNotEquals() {
		// Create a trade for a random user
		UserEntity greekUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity greekTradeMembership = tradeMembershipRandom.nextPersistedEntity(greekUser);
		TradeEntity trade = greekTradeMembership.getTrade();
		
		// Create items for Greek letters
		ArticleEntity alpha = itemRandom.nextPersistedEntity(greekTradeMembership);
		ArticleEntity beta = itemRandom.nextPersistedEntity(greekTradeMembership);
		
		// Create items for country names
		UserEntity countryUser = userRandom.nextPersistedEntity();
		TradeMembershipEntity countryTradeMembership = tradeMembershipRandom.nextPersistedEntity(trade, countryUser, TradeMembershipEntity.Type.MEMBER);
		itemRandom.nextPersistedEntity(countryTradeMembership);
		itemRandom.nextPersistedEntity(countryTradeMembership);
		itemRandom.nextPersistedEntity(countryTradeMembership);
		
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe(Recipe.ITEMS);
		request.addCriterion("Trade.tradeId", trade.getTradeId());
		request.addCriterion("TradeMembership.tradeMembershipId", countryTradeMembership.getTradeMembershipId(), Operator.AND, Matcher.NOT_EQUALS); 
		
		SearchResult<Json> response = fixture.post(request, 1, 10);
		assertEquals(2, response.getResultList().size());
		assertTrue(containsItem(response, alpha));
		assertTrue(containsItem(response, beta));
	}

	private boolean containsItem(SearchResult<Json> response, ArticleEntity targetItem) {
		return response.getResultList().stream().map(i -> {
			ItemJson responseItem = (ItemJson) i;
			return responseItem;
		})
		.anyMatch(v -> v.getName().equals(targetItem.getName()));
	}

}
