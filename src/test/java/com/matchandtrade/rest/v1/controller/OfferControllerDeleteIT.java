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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.OfferRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerDeleteIT {
	
	private OfferController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private OfferRepositoryFacade offerRepositoryFacade;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private MembershipRandom membershipRandom;
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
		TradeEntity trade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), trade);
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerMembership);
		
		// Create member's articles (country names)
		MembershipEntity memberMemberhip = membershipRandom.createPersistedEntity(userRandom.createPersistedEntity(), trade, MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(memberMemberhip);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.createPersistedEntity(ownerMembership.getMembershipId(), alpha.getArticleId(), australia.getArticleId());

		fixture.delete(ownerMembership.getMembershipId(), alphaForAustralia.getOfferId());
		assertNull(offerRepositoryFacade.get(alphaForAustralia.getOfferId()));
	}

	@Test(expected=RestException.class)
	public void shouldNotDeleteWhenMembershipDoesNotBelongToAuthenticatedUser() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), trade);
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerMembership);
		
		// Create member's articles (country names)
		MembershipEntity memberMembership = membershipRandom.createPersistedEntity(userRandom.createPersistedEntity(), trade, MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(memberMembership);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.createPersistedEntity(ownerMembership.getMembershipId(), alpha.getArticleId(), australia.getArticleId());

		try {
			fixture.delete(memberMembership.getMembershipId(), alphaForAustralia.getOfferId());
		} catch (RestException e) {
			assertTrue(e.getMessage().contains("Membership.membershipId must belong to the authenticated User"));
			throw e;
		}
	}

}
