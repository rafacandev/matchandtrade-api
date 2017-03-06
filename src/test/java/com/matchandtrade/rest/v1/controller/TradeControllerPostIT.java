package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.validator.ValidationException;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerPostIT {
	
	@Autowired
	private MockFactory mockFactory;
	@Autowired
	private TradeController tradeController;
	private UserAuthentication userAuthentication;
	private MockHttpServletRequest httpRequest;

	@Before
	public void before() {
		// Let reuse userAuthentication and httpRequest to avoid unnecessary trips to the persistance layer
		if (userAuthentication == null) {
			userAuthentication = mockFactory.nextRandomUserAuthenticationPersisted();
		}
		if (httpRequest == null) {
			httpRequest = mockFactory.getHttpRequestWithAuthenticatedUser(userAuthentication);
		}
	}
	
	@Test
	public void postPositive() {
		tradeController.setHttpServletRequest(httpRequest);
		TradeJson requestJson = TradeRandom.next();
		TradeJson responseJson = tradeController.post(requestJson);
		assertNotNull(responseJson.getTradeId());
		assertEquals(requestJson.getName(), responseJson.getName());
	}
	
	@Test(expected=ValidationException.class)
	public void postNegativeValidationSameName() {
		tradeController.setHttpServletRequest(httpRequest);
		TradeJson requestJson = TradeRandom.next();
		tradeController.post(requestJson);
		tradeController.post(requestJson);
	}

	@Test(expected=ValidationException.class)
	public void postNegativeValidationNameLegth() {
		tradeController.setHttpServletRequest(httpRequest);
		TradeJson requestJson = TradeRandom.next();
		requestJson.setName("ab");
		tradeController.post(requestJson);
	}	
	
	@Test(expected=ValidationException.class)
	public void postNegativeValidationNameMandatory() {
		tradeController.setHttpServletRequest(httpRequest);
		TradeJson requestJson = TradeRandom.next();
		requestJson.setName(null);
		tradeController.post(requestJson);
	}	

}
