package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.repository.TradeRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerPutIT {
	
	@Autowired
	private MockControllerFactory mockControllerFactory;
	private TradeController fixture;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeRepository tradeRepository;
	private TradeTransformer tradeTransformer = new TradeTransformer();

	@Before
	public void before() {
		fixture = mockControllerFactory.getTradeController();
	}
	
	@Test
	public void shouldEditTrade() {
		TradeEntity existingTrade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		existingTrade.setState(TradeEntity.State.MATCHING_ARTICLES);
		TradeJson request = tradeTransformer.transform(existingTrade);
		request.setName(request.getName() + " - Trade.name after PUT");
		request.setDescription(request.getName() + "- Trade.description after PUT");
		TradeJson response = fixture.put(request.getTradeId(), request);
		assertEquals(request.getName(), response.getName());
		assertEquals(request.getDescription(), response.getDescription());
		assertEquals(TradeJson.State.MATCHING_ARTICLES, response.getState());
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenEditingTradeOfDifferentOwner() {
		TradeEntity existingTrade = tradeRandom.createPersistedEntity(userRandom.createPersistedEntity());
		TradeJson request = tradeTransformer.transform(existingTrade);
		fixture.put(request.getTradeId(), request);
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorWhenEditingTradeWithAnExistingName() {
		TradeEntity existingTrade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeEntity anotherExistingTrade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeJson request = new TradeJson();
		request.setName(existingTrade.getName());
		request.setState(TradeJson.State.SUBMITTING_ARTICLES);
		fixture.put(anotherExistingTrade.getTradeId(), request);
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenTryingToEditInexistingTrade() {
		TradeJson request = TradeRandom.createJson();
		fixture.put(-1, request);
	}

	@Test
	public void shouldSaveTradeAndTriggerResultsGenerationWhenStateIsGenerateResults() {
		TradeEntity existingTrade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeJson request = tradeTransformer.transform(existingTrade);
		request.setState(TradeJson.State.GENERATE_RESULTS);
		TradeJson response = fixture.put(existingTrade.getTradeId(), request);		
		assertEquals(TradeJson.State.GENERATE_RESULTS, response.getState());
		TradeEntity actualTrade = tradeRepository.findOne(response.getTradeId());
		
		/*
		 * It is kind of tricky to assert the results in a Integration Test.
		 * They could be either GENERATING_RESULTS or RESULTS_GENERATED, it depends on how fast the sever processes the results.
		 */
		boolean isActualStateIsGeneratingOrGenerated = 
				(actualTrade.getState() == TradeEntity.State.GENERATING_RESULTS || actualTrade.getState() == TradeEntity.State.RESULTS_GENERATED);
		assertEquals(true, isActualStateIsGeneratingOrGenerated);
	}

}
