package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.ItemRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.TestQuery;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ItemControllerDeleteIT {

	private ItemController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private TestQuery testQuery;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getItemController(true);
		}
	}
	
	@Test
	public void shouldDeleteItem() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		fixture.delete(membership.getTradeMembershipId(), item.getItemId());
		ItemEntity foundItem = itemRepository.findOne(item.getItemId());
		assertNull(foundItem);
	}

	@Test(expected=RestException.class)
	public void shouldErrorWhenDeletingItemBelongingToADifferentUser() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ItemEntity item = itemRandom.nextPersistedEntity(membership);
		try {
			fixture.delete(membership.getTradeMembershipId(), item.getItemId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}
	
	@Test(expected=RestException.class)
	public void shouldErrorWhenDeletingItemWithInvalidId() {
		TradeMembershipEntity membership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.delete(membership.getTradeMembershipId(), -1);
	}
	
	@Test
	public void shouldDeleteOffersWhenDeletingItem() {
		TradeMembershipEntity owner = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		TradeMembershipEntity member = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ItemEntity item1 = itemRandom.nextPersistedEntity(owner, "item1");
		ItemEntity item2 = itemRandom.nextPersistedEntity(owner, "item2");
		ItemEntity item3 = itemRandom.nextPersistedEntity(member, "item3");
		ItemEntity item4 = itemRandom.nextPersistedEntity(member, "item4");
		// Offer to be deleted
		offerRandom.nextPersistedEntity(owner.getTradeMembershipId(), item1.getItemId(), item3.getItemId());
		offerRandom.nextPersistedEntity(owner.getTradeMembershipId(), item1.getItemId(), item4.getItemId());
		offerRandom.nextPersistedEntity(member.getTradeMembershipId(), item4.getItemId(), item1.getItemId());
		// Offer to keep
		OfferEntity offerToKeep1 = offerRandom.nextPersistedEntity(owner.getTradeMembershipId(), item2.getItemId(), item3.getItemId());
		OfferEntity offerToKeep2 = offerRandom.nextPersistedEntity(member.getTradeMembershipId(), item4.getItemId(), item2.getItemId());
		fixture.delete(owner.getTradeMembershipId(), item1.getItemId());
		// Assert if offers to keep was not deleted
		List<OfferEntity> offersOwner = testQuery.findOffersByTradeMembership(owner.getTradeMembershipId());
		assertEquals(1, offersOwner.size());
		assertTrue(offersOwner.contains(offerToKeep1));
		List<OfferEntity> offersMember = testQuery.findOffersByTradeMembership(member.getTradeMembershipId());
		assertEquals(1, offersMember.size());
		assertTrue(offersMember.contains(offerToKeep2));
	}

	
}
