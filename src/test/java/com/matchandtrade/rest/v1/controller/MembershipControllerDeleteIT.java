package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.matchandtrade.persistence.repository.MembershipRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	@Autowired
	private MembershipRepository membershipRepository;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getMembershipController();
		}
	}
	
	@Test
	public void delete_When_MembershipExists_Then_Succeeds() {
		MembershipEntity existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.delete(existingMembership.getMembershipId());
		MembershipEntity actual = membershipRepository.findOne(existingMembership.getMembershipId());
		assertNull(actual);
	}
	
	@Test(expected=RestException.class)
	public void delete_When_MembershipDoesNotExists_Then_NotFound() {
		try {
			fixture.delete(-1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

}
