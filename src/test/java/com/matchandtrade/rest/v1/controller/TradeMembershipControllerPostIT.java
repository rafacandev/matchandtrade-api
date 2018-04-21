package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.repository.TradeRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeMembershipControllerPostIT {
	
	private TradeMembershipController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeMembershipController(false);
		}
	}
	
	@Test
	public void post() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		TradeMembershipJson request = new TradeMembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		TradeMembershipJson response = fixture.post(request);
		assertNotNull(response.getTradeMembershipId());
	}
	
	@Test(expected=RestException.class)
	public void postInvalidTrade() {
		TradeMembershipJson requestJson = new TradeMembershipJson();
		requestJson.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		requestJson.setTradeId(-1);
		fixture.post(requestJson);
	}
	
	@Test(expected=RestException.class)
	public void postInvalidUser() {
		TradeMembershipJson request = new TradeMembershipJson();
		request.setUserId(-1);
		fixture.post(request);
	}

	@Test(expected=RestException.class)
	public void postUniqueTradeIdAndUserId() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		TradeMembershipJson request = new TradeMembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		fixture.post(request);
		fixture.post(request);
	}
	
	@Test
	public void shouldOnlySubscribeToTradeWhenTradeStatusIsSubmitingItems() {
		TradeMembershipJson requestForCanceled = createTradeForRandomExistingUser(TradeEntity.State.CANCELED);
		assertThatThrowsInvalidArgumentException(requestForCanceled);
		TradeMembershipJson requestForGenerateResults = createTradeForRandomExistingUser(TradeEntity.State.GENERATE_RESULTS);
		assertThatThrowsInvalidArgumentException(requestForGenerateResults);
		TradeMembershipJson requestForGeneratingResults = createTradeForRandomExistingUser(TradeEntity.State.GENERATING_RESULTS);
		assertThatThrowsInvalidArgumentException(requestForGeneratingResults);
		TradeMembershipJson requestForItemsMatchedResults = createTradeForRandomExistingUser(TradeEntity.State.ITEMS_MATCHED);
		assertThatThrowsInvalidArgumentException(requestForItemsMatchedResults);
		TradeMembershipJson requestForMatchingItemsResults = createTradeForRandomExistingUser(TradeEntity.State.MATCHING_ITEMS);
		assertThatThrowsInvalidArgumentException(requestForMatchingItemsResults);
		TradeMembershipJson requestForResultsGenerated = createTradeForRandomExistingUser(TradeEntity.State.RESULTS_GENERATED);
		assertThatThrowsInvalidArgumentException(requestForResultsGenerated);
	}

	private void assertThatThrowsInvalidArgumentException(TradeMembershipJson request) {
		boolean invalidSubscription = false;
		try {
			fixture.post(request);
		} catch (RestException e) {
			invalidSubscription = true;
		}
		assertTrue(invalidSubscription);
	}

	private TradeMembershipJson createTradeForRandomExistingUser(TradeEntity.State state) {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		existingTrade.setState(state);
		tradeRepository.save(existingTrade);
		TradeMembershipJson request = new TradeMembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(userRandom.nextPersistedEntity().getUserId());
		return request;
	}

}
