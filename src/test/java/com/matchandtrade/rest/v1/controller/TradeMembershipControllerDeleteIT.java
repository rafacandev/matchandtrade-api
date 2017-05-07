package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeMembershipControllerDeleteIT {
	
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
	public void deletePositive() {
		TradeMembershipJson postRequest = tradeMembershipRandom.nextJson();
		TradeMembershipJson postResponse = fixture.post(postRequest);
		fixture.delete(postResponse.getTradeMembershipId());
		TradeMembershipJson getResponse = fixture.get(postResponse.getTradeMembershipId());
		assertNull(getResponse);
	}
	
	@Test(expected=RestException.class)
	public void deleteNegative() {
		fixture.delete(-1);
	}

}
