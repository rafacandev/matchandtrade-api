package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerGetIT {
	
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
	public void getPositive() {
		TradeJson requestJson = TradeRandom.nextJson();
		TradeJson responseJsonPost = fixture.post(requestJson);
		TradeJson responseJsonGet = fixture.get(responseJsonPost.getTradeId());
		assertNotNull(responseJsonGet.getTradeId());
		assertEquals(requestJson.getName(), responseJsonGet.getName());
	}

	@Test
	public void getPositiveParameters() {
		TradeJson requestJson = TradeRandom.nextJson();
		TradeJson responseJsonPost = fixture.post(requestJson);
		SearchResult<TradeJson> responseJsonGet = fixture.get(requestJson.getName(), null, null);
		assertTrue(responseJsonGet.getResultList().contains(responseJsonPost));
	}
	
	@Test
	public void getNegative() {
		TradeJson responseJsonGet = fixture.get(-1);
		assertNull(responseJsonGet);
	}

}
