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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.MembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipControllerGetIT {
	
	private MembershipController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipRandom membershipRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getMembershipController();
		}
	}
	
	@Test
	public void get() {
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		MembershipJson response = fixture.get(existingMembership.getMembershipId());
		assertEquals(existingMembership.getMembershipId(), response.getMembershipId());
		assertEquals(existingMembership.getTrade().getTradeId(), response.getTradeId());
		assertEquals(existingMembership.getUser().getUserId(), response.getUserId());
		assertEquals(MembershipJson.Type.OWNER, response.getType());
	}
	
	@Test
	public void getAll() {
		membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<MembershipJson> getResponse = fixture.get(null, null, null, null, null);
		assertTrue(getResponse.getResultList().size() > 0);
	}
	
	@Test
	public void getByTradeId() {
		fixture = mockControllerFactory.getMembershipController();
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<MembershipJson> getResponse = fixture.get(existingMembership.getTrade().getTradeId(), null, null, null, null);
		assertEquals(existingMembership.getTrade().getTradeId(), getResponse.getResultList().get(0).getTradeId());
	}

	@Test
	public void getByTradeIdAndUserId() {
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<MembershipJson> getResponse = fixture.get(existingMembership.getTrade().getTradeId(), existingMembership.getUser().getUserId(), null, null, null);
		assertEquals(existingMembership.getTrade().getTradeId(), getResponse.getResultList().get(0).getTradeId());
		assertEquals(existingMembership.getUser().getUserId(), getResponse.getResultList().get(0).getUserId());
	}
	
	@Test
	public void getByUserId() {
		fixture = mockControllerFactory.getMembershipController();
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<MembershipJson> getResponse = fixture.get(null, existingMembership.getUser().getUserId(), null, null, null);
		assertEquals(existingMembership.getUser().getUserId(), getResponse.getResultList().get(0).getUserId());
	}

	@Test
	public void shouldGetByTypeAndTradeId() {
		fixture = mockControllerFactory.getMembershipController();
		UserEntity user = fixture.authenticationProvider.getAuthentication().getUser();
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(user);
		SearchResult<MembershipJson> response = fixture.get(existingMembership.getTrade().getTradeId(), null, MembershipEntity.Type.OWNER, null, null);
		assertEquals(1, response.getResultList().size());
		assertEquals(existingMembership.getMembershipId(), response.getResultList().get(0).getMembershipId());
	}
		
	@Test(expected=RestException.class)
	public void getInvalidPageSize() {
		membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		SearchResult<MembershipJson> getResponse = fixture.get(null, null, null, null, 51);
		assertTrue(getResponse.getResultList().size() > 0);
	}
	
	@Test
	public void getInvalidMembershipId() {
		MembershipJson response = fixture.get(-1);
		assertNull(response);
	}
}
