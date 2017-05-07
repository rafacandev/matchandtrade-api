package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.StringRandom;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerPutIT {
	
	@Autowired
	private MockControllerFactory mockControllerFactory;
	private TradeController fixture;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeController(true);
		}
	}
	
	@Test
	public void putPositive() {
		TradeJson randomTrade = TradeRandom.nextJson();
		TradeJson tradePostResponse = fixture.post(randomTrade);
		String randomName = "Name after PUT " + StringRandom.nextName();
		tradePostResponse.setName(randomName);
		TradeJson tradePutResponse = fixture.put(tradePostResponse.getTradeId(), tradePostResponse);
		assertEquals(randomName, tradePutResponse.getName());
	}

	@Test(expected=RestException.class)
	public void putNegativeNotFound() {
		// Try to PUT a trade that does not exist
		TradeJson tradePutRequest = TradeRandom.nextJson();
		tradePutRequest.setTradeId(-1);
		fixture.put(-1, tradePutRequest);
	}
	
	@Test(expected=RestException.class)
	public void putNegativeNotTradeOwner() {
		// Create a new trade. By default the owner is the authenticated user
		TradeJson tradePostResponse = fixture.post(TradeRandom.nextJson());
		String randomName = "Name after PUT " + StringRandom.nextName();
		tradePostResponse.setName(randomName);
		// Get a new TradeController with new authentication
		fixture = mockControllerFactory.getTradeController(false);
		// Try to PUT as a different (not a trade owner)
		fixture.put(tradePostResponse.getTradeId(), tradePostResponse);
	}
	
}
