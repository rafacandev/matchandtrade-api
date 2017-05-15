package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerPutIT {
	
	@Autowired
	private MockControllerFactory mockControllerFactory;
	private TradeController fixture;
	@Autowired
	private TradeRandom tradeRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeController(true);
		}
	}
	
	@Test
	public void put() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeJson tradeRequest = TradeTransformer.transform(existingTrade);
		tradeRequest.setName(tradeRequest.getName() + " - Name after PUT");
		TradeJson tradeResponse = fixture.put(tradeRequest.getTradeId(), tradeRequest);
		assertEquals(tradeRequest.getName(), tradeResponse.getName());
	}

	@Test(expected=RestException.class)
	public void putNotFound() {
		// Try to PUT a trade that does not exist
		TradeJson tradePutRequest = TradeRandom.nextJson();
		fixture.put(-1, tradePutRequest);
	}
	
	@Test(expected=RestException.class)
	public void putNotTradeOwner() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeJson tradeRequest = TradeTransformer.transform(existingTrade);
		mockControllerFactory.getTradeController(false);
		fixture.put(tradeRequest.getTradeId(), tradeRequest);
	}
	
}
