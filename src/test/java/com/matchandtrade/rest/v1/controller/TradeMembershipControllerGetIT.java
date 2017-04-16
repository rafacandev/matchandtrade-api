package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeMembershipControllerGetIT {
	
	private TradeMembershipController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeMembershipController();
		}
	}
	
	@Test
	public void getPositive() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		TradeMembershipJson getResponse = fixture.get(postResponse.getTradeMembershipId());
		assertEquals(postResponse.getTradeMembershipId(), getResponse.getTradeMembershipId());
		assertEquals(postRequest.getTradeId(), getResponse.getTradeId());
		assertEquals(postRequest.getUserId(), getResponse.getUserId());
	}
	
	@Test
	public void getNegativeInvalidTradeMembershipId() {
		TradeMembershipJson response = fixture.get(-1);
		assertNull(response);
	}
	
	@Test
	public void getPositiveParameters() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		SearchResult<TradeMembershipJson> getResponse = fixture.get(postResponse.getTradeId(), postResponse.getUserId(), null, null);
		assertTrue(getResponse.getResultList().contains(postResponse));
	}
	
}
