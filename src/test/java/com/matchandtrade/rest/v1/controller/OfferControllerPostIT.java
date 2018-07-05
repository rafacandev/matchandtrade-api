package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerPostIT {
	
	private OfferController fixture;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRandom userRandom;

	private void assertOffer(ItemEntity offered, ItemEntity wanted, OfferJson json) {
		assertNotNull(json.getOfferId());
		assertEquals(offered.getArticleId(), json.getOfferedArticleId());
		assertEquals(wanted.getArticleId(), json.getWantedArticleId());
	}
	
	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getOfferController(true);
		}
	}

	@Test
	public void shouldMemberOfferVariousItemsToOwner() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		ItemEntity beta = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ItemEntity cuba = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner offers Alpha for Australia
		OfferJson alphaForAustralia = OfferRandom.nextJson(alpha.getArticleId(), australia.getArticleId());
		alphaForAustralia = fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alphaForAustralia);
		assertOffer(alpha, australia, alphaForAustralia);
		
		// Owner offers Alpha for Cuba
		OfferJson alphaForCuba = OfferRandom.nextJson(alpha.getArticleId(), cuba.getArticleId());
		alphaForCuba = fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alphaForCuba);
		assertOffer(alpha, cuba, alphaForCuba);

		// Member offers Beta for Brazil
		OfferJson betaForBrazil = OfferRandom.nextJson(beta.getArticleId(), brazil.getArticleId());
		betaForBrazil = fixture.post(ownerTradeMemberhip.getTradeMembershipId(), betaForBrazil);
		assertOffer(beta, brazil, betaForBrazil);

		// Member offers Australia for Alpha
		OfferJson australiaForAlpha = OfferRandom.nextJson(australia.getArticleId(), alpha.getArticleId());
		australiaForAlpha = memberController.post(memberTradeMemberhip.getTradeMembershipId(), australiaForAlpha);
		assertOffer(australia, alpha, australiaForAlpha);
		
		// Member offers Cuba for Beta
		OfferJson cubaForAlpha = OfferRandom.nextJson(cuba.getArticleId(), alpha.getArticleId());
		cubaForAlpha = memberController.post(memberTradeMemberhip.getTradeMembershipId(), cubaForAlpha);
		assertOffer(cuba, alpha, cubaForAlpha);
	}
	
	@Test(expected=RestException.class)
	public void shouldNotCreateOfferWhenItemDoesNotExist() {
		TradeMembershipEntity ownerMembership = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerMembership);
		OfferJson request = OfferRandom.nextJson(alpha.getArticleId(), 99999999);
		try {
			fixture.post(ownerMembership.getTradeMembershipId(), request);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertTrue(e.getMessage().contains("Offer.offeredArticleId and Offer.wantedArticleId must belong to existing Items"));
			throw e;
		}
	}

	@Test(expected=RestException.class)
	public void shouldNotCreateOfferWhenItemsAreNotAssociatedToTheSameTrade() {
		UserEntity owner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(owner);
		TradeMembershipEntity ownerMembership = tradeMembershipRandom.nextPersistedEntity(trade, owner);
		ItemEntity ownerItem = itemRandom.nextPersistedEntity(ownerMembership);

		UserEntity member = userRandom.nextPersistedEntity();
		TradeMembershipEntity memberMembership = tradeMembershipRandom.nextPersistedEntity(tradeRandom.nextPersistedEntity(member), member, TradeMembershipEntity.Type.MEMBER);
		ItemEntity memberItem = itemRandom.nextPersistedEntity(memberMembership);

		OfferJson request = OfferRandom.nextJson(ownerItem.getArticleId(), memberItem.getArticleId());
		try {
			fixture.post(ownerMembership.getTradeMembershipId(), request);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertTrue(e.getMessage().contains("must be associated to the same Trade"));
			throw e;
		}
	}
	
	@Test(expected=RestException.class)
	public void shouldNotCreateOfferWhenOfferingItemDoesNotBelongToTheOfferingUser() {
		UserEntity owner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(owner);
		TradeMembershipEntity ownerMembership = tradeMembershipRandom.nextPersistedEntity(trade, owner);
		ItemEntity ownerItem = itemRandom.nextPersistedEntity(ownerMembership);

		UserEntity member = userRandom.nextPersistedEntity();
		TradeMembershipEntity memberMembership = tradeMembershipRandom.nextPersistedEntity(trade, member, TradeMembershipEntity.Type.MEMBER);
		ItemEntity memberItem = itemRandom.nextPersistedEntity(memberMembership);

		OfferJson request = OfferRandom.nextJson(memberItem.getArticleId(), ownerItem.getArticleId());
		try {
			fixture.post(ownerMembership.getTradeMembershipId(), request);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertTrue(e.getMessage().contains("Offer.offeredArticleId must belong to the offering User.userId."));
			throw e;
		}
	}

	@Test(expected=RestException.class)
	public void shouldNotCreateTradeWhenTradeMembershipDoesNotBelongToAuthenticatedUser() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser());
		ItemEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, userRandom.nextPersistedEntity(), TradeMembershipEntity.Type.MEMBER);
		ItemEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		OfferJson alphaForAustralia = OfferRandom.nextJson(alpha.getArticleId(), australia.getArticleId());
		alphaForAustralia = fixture.post(ownerTradeMemberhip.getTradeMembershipId(), alphaForAustralia);
		
		// Member offers Australia for Alpha
		OfferJson australiaForAlpha = OfferRandom.nextJson(australia.getArticleId(), alpha.getArticleId());
		try {
			australiaForAlpha = fixture.post(memberTradeMemberhip.getTradeMembershipId(), australiaForAlpha);
		} catch (RestException e) {
			assertTrue(e.getMessage().contains("TradeMembership must belong to the current authenticated User"));
			throw e;
		}
	}

	@Test
	public void shouldOwnerOfferItemToMember() {
		UserEntity owner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.nextPersistedEntity(owner);
		TradeMembershipEntity ownerMembership = tradeMembershipRandom.nextPersistedEntity(trade, owner);
		ItemEntity ownerItem = itemRandom.nextPersistedEntity(ownerMembership);

		UserEntity member = userRandom.nextPersistedEntity();
		TradeMembershipEntity memberMembership = tradeMembershipRandom.nextPersistedEntity(trade, member, TradeMembershipEntity.Type.MEMBER);
		ItemEntity memberItem = itemRandom.nextPersistedEntity(memberMembership);

		OfferJson request = OfferRandom.nextJson(ownerItem.getArticleId(), memberItem.getArticleId());
		OfferJson response = fixture.post(ownerMembership.getTradeMembershipId(), request);
		assertNotNull(response);
		assertNotNull(response.getOfferId());
	}
	
}
