package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerDeleteIT {
	
	private OfferController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getOfferController(true);
		}
	}
	
	@Test
	public void shouldDeleteByOfferId() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerMembership = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity alpha = itemRandom.nextPersistedEntity(ownerMembership);
		
		// Create member's items (country names)
		TradeMembershipEntity memberMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(memberMemberhip);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerMembership.getTradeMembershipId(), alpha.getArticleId(), australia.getArticleId());

		fixture.delete(ownerMembership.getTradeMembershipId(), alphaForAustralia.getOfferId());
		assertNull(offerRepositoryFacade.get(alphaForAustralia.getOfferId()));
	}

	@Test(expected=RestException.class)
	public void shouldNotDeleteWhenTradeMembershipDoesNotBelongToAuthenticatedUser() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerMembership = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity alpha = itemRandom.nextPersistedEntity(ownerMembership);
		
		// Create member's items (country names)
		TradeMembershipEntity memberMembership = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(memberMembership);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerMembership.getTradeMembershipId(), alpha.getArticleId(), australia.getArticleId());

		try {
			fixture.delete(memberMembership.getTradeMembershipId(), alphaForAustralia.getOfferId());
		} catch (RestException e) {
			assertTrue(e.getMessage().contains("TradeMembership.tradeMembershipId must belong to the authenticated User"));
			throw e;
		}
	}

}
