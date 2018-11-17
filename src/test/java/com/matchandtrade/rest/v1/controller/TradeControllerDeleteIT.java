package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.matchandtrade.persistence.repository.TradeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class TradeControllerDeleteIT {
	
	private TradeController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeRepository tradeRepository;
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getTradeController();
		}
	}

	@Test
	public void delete_When_TradeExists_Then_Succeeds() {
		TradeEntity existingTrade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.delete(existingTrade.getTradeId());
		TradeEntity actual = tradeRepository.findOne(existingTrade.getTradeId());
		assertNull(actual);
	}
	
	@Test(expected = RestException.class)
	public void delete_When_TradeDoesNotExistExists_Then_NotFound() {
		try {
			fixture.delete(-1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}
	
}
