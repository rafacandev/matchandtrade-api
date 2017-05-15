package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.rest.v1.transformer.TradeMembershipTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeMembershipControllerPostIT {
	
	private TradeMembershipController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeMembershipController(false);
		}
	}
	
	@Test
	public void post() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		TradeMembershipJson request = new TradeMembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		TradeMembershipJson response = fixture.post(request);
		assertNotNull(response.getTradeMembershipId());
	}
	
	@Test(expected=RestException.class)
	public void postInvalidTrade() {
		TradeMembershipJson requestJson = new TradeMembershipJson();
		requestJson.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		requestJson.setTradeId(-1);
		fixture.post(requestJson);
	}
	
	@Test(expected=RestException.class)
	public void postInvalidUser() {
		TradeMembershipJson request = new TradeMembershipJson();
		request.setUserId(-1);
		fixture.post(request);
	}

	@Test(expected=RestException.class)
	public void postUniqueTradeIdAndUserId() {
		TradeEntity existingTrade = tradeRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		TradeMembershipJson request = new TradeMembershipJson();
		request.setTradeId(existingTrade.getTradeId());
		request.setUserId(fixture.authenticationProvider.getAuthentication().getUser().getUserId());
		fixture.post(request);
		fixture.post(request);
	}

}
