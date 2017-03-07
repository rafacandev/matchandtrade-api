package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.test.MockFactory;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerGetIT {
	
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
	public void getPositive() {
		tradeController.setHttpServletRequest(httpRequest);
		TradeJson requestJson = TradeRandom.next();
		TradeJson responseJsonPost = tradeController.post(requestJson);
		TradeJson responseJsonGet = tradeController.get(responseJsonPost.getTradeId());
		assertNotNull(responseJsonGet.getTradeId());
		assertEquals(requestJson.getName(), responseJsonGet.getName());
	}

	@Test
	public void getNegative() {
		tradeController.setHttpServletRequest(httpRequest);
		TradeJson responseJsonGet = tradeController.get(-1);
		assertNull(responseJsonGet);
	}

}
