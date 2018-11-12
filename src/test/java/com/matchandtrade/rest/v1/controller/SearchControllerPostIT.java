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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.Json;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.search.Matcher;
import com.matchandtrade.rest.v1.json.search.Operator;
import com.matchandtrade.rest.v1.json.search.Recipe;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class SearchControllerPostIT {
	
	private SearchController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MembershipRandom membershipRandom;
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
		UserEntity greekUser = userRandom.createPersistedEntity();
		MembershipEntity greekMembership = membershipRandom.createPersistedEntity(greekUser);
		TradeEntity trade = greekMembership.getTrade();

		// Create articles for Greek letters
		ArticleEntity alpha = articleRandom.nextPersistedEntity(greekMembership);
		ArticleEntity beta = articleRandom.nextPersistedEntity(greekMembership);

		// Create articles for country names
		UserEntity countryUser = userRandom.createPersistedEntity();
		MembershipEntity countryMembership = membershipRandom.createPersistedEntity(countryUser, trade, MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(countryMembership);
		ArticleEntity brazil = articleRandom.nextPersistedEntity(countryMembership);
		ArticleEntity cuba = articleRandom.nextPersistedEntity(countryMembership);
		
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe(Recipe.ARTICLES);
		request.addCriterion("Trade.tradeId", trade.getTradeId());

		SearchResult<Json> response = fixture.post(request, 1, 10);
		assertEquals(5, response.getResultList().size());
		assertTrue(containsArticle(response, alpha));
		assertTrue(containsArticle(response, beta));
		assertTrue(containsArticle(response, australia));
		assertTrue(containsArticle(response, brazil));
		assertTrue(containsArticle(response, cuba));
	}

	@Test
	public void searchByTradeIdAndMembershipIdNotEquals() {
		// Create a trade for a random user
		UserEntity greekUser = userRandom.createPersistedEntity();
		MembershipEntity greekMembership = membershipRandom.createPersistedEntity(greekUser);
		TradeEntity trade = greekMembership.getTrade();
		
		// Create articles for Greek letters
		ArticleEntity alpha = articleRandom.nextPersistedEntity(greekMembership);
		ArticleEntity beta = articleRandom.nextPersistedEntity(greekMembership);
		
		// Create articles for country names
		UserEntity countryUser = userRandom.createPersistedEntity();
		MembershipEntity countryMembership = membershipRandom.createPersistedEntity(countryUser, trade, MembershipEntity.Type.MEMBER);
		articleRandom.nextPersistedEntity(countryMembership);
		articleRandom.nextPersistedEntity(countryMembership);
		articleRandom.nextPersistedEntity(countryMembership);
		
		SearchCriteriaJson request = new SearchCriteriaJson();
		request.setRecipe(Recipe.ARTICLES);
		request.addCriterion("Trade.tradeId", trade.getTradeId());
		request.addCriterion("Membership.membershipId", countryMembership.getMembershipId(), Operator.AND, Matcher.NOT_EQUALS); 
		
		SearchResult<Json> response = fixture.post(request, 1, 10);
		assertEquals(2, response.getResultList().size());
		assertTrue(containsArticle(response, alpha));
		assertTrue(containsArticle(response, beta));
	}

	private boolean containsArticle(SearchResult<Json> response, ArticleEntity targetArticle) {
		return response.getResultList().stream()
			.map(i -> (ArticleJson) i)
			.anyMatch(v -> v.getName().equals(targetArticle.getName()));
	}

}
