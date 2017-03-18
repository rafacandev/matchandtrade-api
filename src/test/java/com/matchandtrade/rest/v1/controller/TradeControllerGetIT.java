package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerGetIT {
	
	private TradeController tradeController;
	@Autowired
	private MockTradeControllerFactory mockTradeControllerFactory;

	@Before
	public void before() {
		if (tradeController == null) {
			tradeController = mockTradeControllerFactory.getMockTradeController();
		}
	}

	@Test
	public void getPositive() {
		TradeJson requestJson = TradeRandom.next();
		TradeJson responseJsonPost = tradeController.post(requestJson);
		TradeJson responseJsonGet = tradeController.get(responseJsonPost.getTradeId());
		assertNotNull(responseJsonGet.getTradeId());
		assertEquals(requestJson.getName(), responseJsonGet.getName());
	}

	@Test
	public void getNegative() {
		TradeJson responseJsonGet = tradeController.get(-1);
		assertNull(responseJsonGet);
	}

}
