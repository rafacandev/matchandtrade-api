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
import com.matchandtrade.rest.RestException;
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
			fixture = mockControllerFactory.getTradeMembershipController(true);
		}
	}
	
	@Test
	public void get() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		TradeMembershipJson getResponse = fixture.get(postResponse.getTradeMembershipId());
		assertEquals(postResponse.getTradeMembershipId(), getResponse.getTradeMembershipId());
		assertEquals(postRequest.getTradeId(), getResponse.getTradeId());
		assertEquals(postRequest.getUserId(), getResponse.getUserId());
	}
	
	@Test
	public void getInvalidTradeMembershipId() {
		TradeMembershipJson response = fixture.get(-1);
		assertNull(response);
	}
	
	@Test
	public void getByTradeIdAndUserId() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		SearchResult<TradeMembershipJson> getResponse = fixture.get(postResponse.getTradeId(), postResponse.getUserId(), null, null);
		assertEquals(postResponse.getTradeId(), getResponse.getResultList().get(0).getTradeId());
		assertEquals(postResponse.getUserId(), getResponse.getResultList().get(0).getUserId());
	}

	@Test
	public void getAll() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		fixture.post(postRequest);
		SearchResult<TradeMembershipJson> getResponse = fixture.get(null, null, null, null);
		assertTrue(getResponse.getResultList().size() > 0);
	}
	
	@Test(expected=RestException.class)
	public void getInvalidPageSize() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		fixture.post(postRequest);
		SearchResult<TradeMembershipJson> getResponse = fixture.get(null, null, null, 51);
		assertTrue(getResponse.getResultList().size() > 0);
	}

		
	@Test
	public void getByUserId() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		SearchResult<TradeMembershipJson> getResponse = fixture.get(null, postResponse.getUserId(), null, null);
		assertEquals(postResponse.getTradeId(), getResponse.getResultList().get(0).getTradeId());
		assertEquals(postResponse.getUserId(), getResponse.getResultList().get(0).getUserId());
	}
	
	@Test
	public void getByTradeId() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		SearchResult<TradeMembershipJson> getResponse = fixture.get(postResponse.getTradeId(), null, null, null);
		assertEquals(postResponse.getTradeId(), getResponse.getResultList().get(0).getTradeId());
		assertEquals(postResponse.getUserId(), getResponse.getResultList().get(0).getUserId());
	}
}
