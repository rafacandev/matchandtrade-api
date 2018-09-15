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
		fixture = mockControllerFactory.getTradeController(true);
	}
	
	@Test
	public void shouldCreateTrade() {
		TradeJson request = TradeRandom.nextJson();
		TradeJson response = fixture.post(request);
		assertNotNull(response.getTradeId());
		assertEquals(request.getName(), response.getName());
		assertEquals(request.getDescription(), response.getDescription());
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorNameAlreadyExists() {
		TradeJson request = TradeRandom.nextJson();
		fixture.post(request);
		fixture.post(request);
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenNameIsTooShort() {
		TradeJson request = TradeRandom.nextJson();
		request.setName("ab");
		fixture.post(request);
	}	

	@Test(expected=RestException.class)
	public void shouldErrorWhenNameIsTooLong() {
		TradeJson request = TradeRandom.nextJson();
		String name = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "01234567890123456789012345678901234567890123456789"
				+ "0";
		request.setName(name);
		fixture.post(request);
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorWhenDescriptionIsTooLong() {
		TradeJson request = TradeRandom.nextJson();
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
		request.setDescription(description);
		fixture.post(request);
	}	
	
	@Test(expected=RestException.class)
	public void shouldErrorWhenNameIsMissing() {
		TradeJson request = TradeRandom.nextJson();
		request.setName(null);
		fixture.post(request);
	}

}
