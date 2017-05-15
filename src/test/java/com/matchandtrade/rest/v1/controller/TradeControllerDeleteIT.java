package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerDeleteIT {
	
	private TradeController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeController(true);
		}
	}

	@Test
	public void deletePositive() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.delete(existingTrade.getTradeId());
		assertNull(fixture.get(existingTrade.getTradeId()));
	}
	
	@Test(expected=RestException.class)
	public void deleteNegativeInvalidTrade() {
		fixture.delete(-1);
	}
	
}
