package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerGetIT {
	
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
	public void get() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeJson response = fixture.get(existingTrade.getTradeId());
		assertNotNull(response.getTradeId());
		assertEquals(existingTrade.getName(), response.getName());
	}

	@Test
	public void getByName() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<TradeJson> response = fixture.get(existingTrade.getName(), null, null);
		assertEquals(existingTrade.getName(), response.getResultList().get(0).getName());
	}
	
	@Test
	public void getInvalid() {
		TradeJson response = fixture.get(-1);
		assertNull(response);
	}

}
