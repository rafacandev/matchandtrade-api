package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class TradeControllerPostIT {
	
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
	public void postPositive() {
		TradeJson requestJson = TradeRandom.nextJson();
		TradeJson responseJson = fixture.post(requestJson);
		assertNotNull(responseJson.getTradeId());
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=RestException.class)
	public void postNegativeValidationSameName() {
		TradeJson requestJson = TradeRandom.nextJson();
		fixture.post(requestJson);
		fixture.post(requestJson);
	}

	@Test(expected=RestException.class)
	public void postNegativeValidationNameLegth() {
		TradeJson requestJson = TradeRandom.nextJson();
		requestJson.setName("ab");
		fixture.post(requestJson);
	}	
	
	@Test(expected=RestException.class)
	public void postNegativeValidationNameMandatory() {
		TradeJson requestJson = TradeRandom.nextJson();
		requestJson.setName(null);
		fixture.post(requestJson);
	}	

}
