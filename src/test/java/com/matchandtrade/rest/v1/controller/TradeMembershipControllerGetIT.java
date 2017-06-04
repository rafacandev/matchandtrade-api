package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
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
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeMembershipJson response = fixture.get(existingTradeMembership.getTradeMembershipId());
		assertEquals(existingTradeMembership.getTradeMembershipId(), response.getTradeMembershipId());
		assertEquals(existingTradeMembership.getTrade().getTradeId(), response.getTradeId());
		assertEquals(existingTradeMembership.getUser().getUserId(), response.getUserId());
		assertEquals(TradeMembershipJson.Type.OWNER, response.getType());
	}
	
	@Test
	public void getAll() {
		tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<TradeMembershipJson> getResponse = fixture.get(null, null, null, null);
		assertTrue(getResponse.getResultList().size() > 0);
	}
	
	@Test
	public void getByTradeId() {
		fixture = mockControllerFactory.getTradeMembershipController(false);
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<TradeMembershipJson> getResponse = fixture.get(existingTradeMembership.getTrade().getTradeId(), null, null, null);
		assertEquals(existingTradeMembership.getTrade().getTradeId(), getResponse.getResultList().get(0).getTradeId());
		assertEquals(existingTradeMembership.getUser().getUserId(), getResponse.getResultList().get(0).getUserId());
	}

	@Test
	public void getByTradeIdAndUserId() {
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<TradeMembershipJson> getResponse = fixture.get(existingTradeMembership.getTrade().getTradeId(), existingTradeMembership.getUser().getUserId(), null, null);
		assertEquals(existingTradeMembership.getTrade().getTradeId(), getResponse.getResultList().get(0).getTradeId());
		assertEquals(existingTradeMembership.getUser().getUserId(), getResponse.getResultList().get(0).getUserId());
	}
	
	@Test
	public void getByUserId() {
		fixture = mockControllerFactory.getTradeMembershipController(false);
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<TradeMembershipJson> getResponse = fixture.get(null, existingTradeMembership.getUser().getUserId(), null, null);
		assertEquals(existingTradeMembership.getUser().getUserId(), getResponse.getResultList().get(0).getUserId());
	}

		
	@Test(expected=RestException.class)
	public void getInvalidPageSize() {
		tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<TradeMembershipJson> getResponse = fixture.get(null, null, null, 51);
		assertTrue(getResponse.getResultList().size() > 0);
	}
	
	@Test
	public void getInvalidTradeMembershipId() {
		TradeMembershipJson response = fixture.get(-1);
		assertNull(response);
	}
}
