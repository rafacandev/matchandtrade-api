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
			fixture = mockControllerFactory.getTradeController(true);
		}
	}
	
	@Test
	public void shouldCreateTrade() {
		TradeJson requestJson = TradeRandom.nextJson();
		TradeJson responseJson = fixture.post(requestJson);
		assertNotNull(responseJson.getTradeId());
		assertEquals(requestJson.getName(), responseJson.getName());
		assertEquals(requestJson.getDescription(), responseJson.getDescription());
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorNameAlreadyExists() {
		TradeJson requestJson = TradeRandom.nextJson();
		fixture.post(requestJson);
		fixture.post(requestJson);
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenNameIsTooShort() {
		TradeJson requestJson = TradeRandom.nextJson();
		requestJson.setName("ab");
		fixture.post(requestJson);
	}	

	@Test(expected=RestException.class)
	public void shouldErrorWhenNameIsTooLong() {
		TradeJson requestJson = TradeRandom.nextJson();
		String name = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "01234567890123456789012345678901234567890123456789"
				+ "0";
		requestJson.setName(name);
		fixture.post(requestJson);
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorWhenDescriptionIsTooLong() {
		TradeJson requestJson = TradeRandom.nextJson();
		String description = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0";
		requestJson.setDescription(description);
		fixture.post(requestJson);
	}	
	
	@Test(expected=RestException.class)
	public void shouldErrorWhenNameIsMissing() {
		TradeJson requestJson = TradeRandom.nextJson();
		requestJson.setName(null);
		fixture.post(requestJson);
	}

}
