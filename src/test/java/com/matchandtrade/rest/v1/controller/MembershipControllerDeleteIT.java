package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.MembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipControllerDeleteIT {
	
	private MembershipController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipRandom membershipRandom;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getMembershipController(true);
		}
	}
	
	@Test
	public void delete() {
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.delete(existingMembership.getMembershipId());
		assertNull(fixture.get(existingMembership.getMembershipId()));
	}
	
	@Test(expected=RestException.class)
	public void deleteInvalidTrades() {
		fixture.delete(-1);
	}

}
