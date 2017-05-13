package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.ItemRepository;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemControllerGetIT {

	private ItemController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private ItemRepository itemRepository;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemController(true);
		}
	}

	@Test
 	public void positiveGetOne() {
		// Create itemEntity
		ItemEntity itemEntity = ItemRandom.nextEntity();
		itemRepository.save(itemEntity);
		// Add the itemEntity to a TradeMembership
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMemberhipEntity.getItems().add(itemEntity);
		tradeMembershipRepository.save(tradeMemberhipEntity);
		// GET /trade-memberships/{tradeMembershipId}/items/{itemId}
		ItemJson response = fixture.get(tradeMemberhipEntity.getTradeMembershipId(), itemEntity.getItemId());
		assertNotNull(response);
	}

	@Test
	public void positiveSearchByName() {
		// Create itemEntity
		ItemEntity itemEntity = ItemRandom.nextEntity();
		itemRepository.save(itemEntity);
		// Add the itemEntity to a TradeMembership
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMemberhipEntity.getItems().add(itemEntity);
		tradeMembershipRepository.save(tradeMemberhipEntity);
		// GET /trade-memberships/{tradeMembershipId}/items/{itemId}
		SearchResult<ItemJson> response = fixture.get(tradeMemberhipEntity.getTradeMembershipId(), itemEntity.getName(), null, null);
		assertEquals(itemEntity.getName(), response.getResultList().get(0).getName());
		for (ItemJson i : response.getResultList()) {
			System.out.println(i.getLinks().size());
			for(Link l : i.getLinks()) {
				System.out.println(l.getHref());
			}
		}
	}

	@Test
 	public void positiveGetAll() {
		// Create itemEntity
		ItemEntity item1 = ItemRandom.nextEntity();
		itemRepository.save(item1);
		ItemEntity item2 = ItemRandom.nextEntity();
		itemRepository.save(item2);
		ItemEntity item3 = ItemRandom.nextEntity();
		itemRepository.save(item3);
		// Add the itemEntity to a TradeMembership
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMemberhipEntity.getItems().add(item1);
		tradeMemberhipEntity.getItems().add(item2);
		tradeMemberhipEntity.getItems().add(item3);
		tradeMembershipRepository.save(tradeMemberhipEntity);
		// GET /trade-memberships/{tradeMembershipId}/items/
		SearchResult<ItemJson> response = fixture.get(tradeMemberhipEntity.getTradeMembershipId(), null, null, null);
		assertEquals(3, response.getResultList().size());
		
		for (ItemJson i : response.getResultList()) {
			System.out.println(i.getLinks().size());
			for(Link l : i.getLinks()) {
				System.out.println(l.getHref());
			}
		}
		
	}

	
	@Test(expected = RestException.class)
 	public void negativeUserNotAssociatedWithTradeMembership() {
		// Create itemEntity
		ItemEntity itemEntity = ItemRandom.nextEntity();
		itemRepository.save(itemEntity);
		// Add the itemEntity to a TradeMembership
		TradeMembershipEntity tradeMemberhipEntity = tradeMembershipRandom.nextEntity(fixture.authenticationProvider.getAuthentication().getUser());
		tradeMemberhipEntity.getItems().add(itemEntity);
		tradeMembershipRepository.save(tradeMemberhipEntity);
		// GET /trade-memberships/{tradeMembershipId}/items
		fixture = mockControllerFactory.getItemController(false);
		fixture.get(tradeMemberhipEntity.getTradeMembershipId(), itemEntity.getItemId());
	}

	@Test(expected = RestException.class)
 	public void negativeTradeMembershipNotFound() {
		// Create itemEntity
		ItemEntity itemEntity = ItemRandom.nextEntity();
		itemRepository.save(itemEntity);
		// GET /trade-memberships/{tradeMembershipId}/items
		fixture.get(-1, itemEntity.getItemId());
	}

}
