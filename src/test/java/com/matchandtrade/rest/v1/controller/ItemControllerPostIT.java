package com.matchandtrade.rest.v1.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
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
			fixture = mockControllerFactory.getItemController();
		}
	}
	
	@Test
	public void post() {
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity();
		tradeMemberhipEntity.setUser(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMembershipRepository.save(tradeMemberhipEntity);
		ItemJson item = itemRandom.nextJson(tradeMemberhipEntity.getTradeMembershipId());
		item.setName(StringRandom.nextName());
		fixture.post(tradeMemberhipEntity.getTradeMembershipId(), item);
	}

}
