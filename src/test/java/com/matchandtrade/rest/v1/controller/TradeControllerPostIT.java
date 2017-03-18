package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.validator.ValidationException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerPostIT {
	
	@Autowired
	private static TradeController tradeController;
	@Autowired
	private MockTradeControllerFactory mockTradeControllerFactory;

	@Before
	public void before() {
		if (tradeController == null) {
			tradeController = mockTradeControllerFactory.getMockTradeController();
		}
	}
	
	@Test
	public void postPositive() {
		TradeJson requestJson = TradeRandom.next();
		TradeJson responseJson = tradeController.post(requestJson);
		assertNotNull(responseJson.getTradeId());
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=ValidationException.class)
	public void postNegativeValidationSameName() {
		TradeJson requestJson = TradeRandom.next();
		tradeController.post(requestJson);
		tradeController.post(requestJson);
	}

	@Test(expected=ValidationException.class)
	public void postNegativeValidationNameLegth() {
		TradeJson requestJson = TradeRandom.next();
		requestJson.setName("ab");
		tradeController.post(requestJson);
	}	
	
	@Test(expected=ValidationException.class)
	public void postNegativeValidationNameMandatory() {
		TradeJson requestJson = TradeRandom.next();
		requestJson.setName(null);
		tradeController.post(requestJson);
	}	

}
