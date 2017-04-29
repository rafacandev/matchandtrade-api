package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerDeleteIT {
	
	private TradeController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeController();
		}
	}

	@Test
	public void deletePositive() {
		TradeJson requestJson = TradeRandom.nextJson();
		TradeJson responseJsonPost = fixture.post(requestJson);
		fixture.delete(responseJsonPost.getTradeId());
		assertNull(fixture.get(responseJsonPost.getTradeId()));
	}
	
	@Test(expected=RestException.class)
	public void deleteNegativeInvalidTrade() {
		fixture.delete(-1);
	}
	
	@Test(expected=RestException.class)
	public void deleteNegativeTradeIsNull() {
		fixture.delete(null);
	}
}
