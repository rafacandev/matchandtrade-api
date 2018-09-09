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
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipControllerPostIT {
	
	private MembershipController fixture;
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
			fixture = mockControllerFactory.getMembershipController(false);
		}
	}
	
	@Test
	public void post() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		MembershipJson request = new MembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		MembershipJson response = fixture.post(request);
		assertNotNull(response.getMembershipId());
	}
	
	@Test(expected=RestException.class)
	public void postInvalidTrade() {
		MembershipJson requestJson = new MembershipJson();
		requestJson.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		requestJson.setTradeId(-1);
		fixture.post(requestJson);
	}
	
	@Test(expected=RestException.class)
	public void postInvalidUser() {
		MembershipJson request = new MembershipJson();
		request.setUserId(-1);
		fixture.post(request);
	}

	@Test(expected=RestException.class)
	public void postUniqueTradeIdAndUserId() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		MembershipJson request = new MembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		fixture.post(request);
		fixture.post(request);
	}
	
	@Test
	public void shouldOnlySubscribeToTradeWhenTradeStatusIsSubmitingArticles() {
		MembershipJson requestForCanceled = createTradeForRandomExistingUser(TradeEntity.State.CANCELED);
		assertThatThrowsInvalidArgumentException(requestForCanceled);
		MembershipJson requestForGenerateResults = createTradeForRandomExistingUser(TradeEntity.State.GENERATE_RESULTS);
		assertThatThrowsInvalidArgumentException(requestForGenerateResults);
		MembershipJson requestForGeneratingResults = createTradeForRandomExistingUser(TradeEntity.State.GENERATING_RESULTS);
		assertThatThrowsInvalidArgumentException(requestForGeneratingResults);
		MembershipJson requestForArticlesMatchedResults = createTradeForRandomExistingUser(TradeEntity.State.ARTICLES_MATCHED);
		assertThatThrowsInvalidArgumentException(requestForArticlesMatchedResults);
		MembershipJson requestForMatchingArticlesResults = createTradeForRandomExistingUser(TradeEntity.State.MATCHING_ARTICLES);
		assertThatThrowsInvalidArgumentException(requestForMatchingArticlesResults);
		MembershipJson requestForResultsGenerated = createTradeForRandomExistingUser(TradeEntity.State.RESULTS_GENERATED);
		assertThatThrowsInvalidArgumentException(requestForResultsGenerated);
	}

	private void assertThatThrowsInvalidArgumentException(MembershipJson request) {
		boolean invalidSubscription = false;
		try {
			fixture.post(request);
		} catch (RestException e) {
			invalidSubscription = true;
		}
		assertTrue(invalidSubscription);
	}

	private MembershipJson createTradeForRandomExistingUser(TradeEntity.State state) {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		existingTrade.setState(state);
		tradeRepository.save(existingTrade);
		MembershipJson request = new MembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(userRandom.nextPersistedEntity().getUserId());
		return request;
	}

}
