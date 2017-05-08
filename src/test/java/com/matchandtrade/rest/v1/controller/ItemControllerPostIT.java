package com.matchandtrade.rest.v1.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.StringRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemControllerPostIT {

	private ItemController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemController(true);
		}
	}

	@Test
	public void post() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName(StringRandom.nextName());
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}
	
	@Test(expected=RestException.class)
	public void postNegativeNameIsNull() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName(null);
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}
	
	@Test(expected=RestException.class)
	public void postNegativeUserNotAssociatedWithTradeMembership() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName(StringRandom.nextName());
		fixture = mockControllerFactory.getItemController(false);
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}

	@Test(expected = RestException.class)
	public void postNegativeCannotHaveDuplicatedName() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName(StringRandom.nextName());
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}
	
	@Test(expected = RestException.class)
	public void postNegativeShortName() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName("ab");
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}

	@Test(expected = RestException.class)
	public void postNegativeLongName() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName("151-characters-long-151-characters-long-151-characters-long-151-characters-long-151-characters-long-151-characters-long-151-characters-long-151-charact");
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}

	@Test(expected = RestException.class)
	public void postNegativeTradeMembershipNotFound() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity);
		item.setName(StringRandom.nextName());
		fixture.post(-1, item);
	}
	
}
