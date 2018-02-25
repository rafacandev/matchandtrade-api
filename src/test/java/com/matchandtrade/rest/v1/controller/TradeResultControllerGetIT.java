package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;


@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeResultControllerGetIT {
	
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	private TradeResultController fixture;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeResultController(true);
		}
	}
	
	@Test(expected = RestException.class)
	public void resultsAreOnlyGeneratedIfTradeStatusIsMatchingItemsEndedOrGeneratingTradesEnded() throws IOException {
		UserEntity tradeOwner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(tradeOwner);
		try {
			fixture.getText(trade.getTradeId());
		} catch (RestException e) {
			assertEquals("TradeResult is only availble when Trade.State is GENERATE_RESULTS, GENERATING_RESULTS, RESULTS_GENERATED.", e.getDescription());
			throw e;
		}
	}

}
