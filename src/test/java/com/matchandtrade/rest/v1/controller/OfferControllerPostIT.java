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

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.TradeRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerPostIT {
	
	private OfferController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private UserRandom userRandom;

	private void assertOffer(ArticleEntity offered, ArticleEntity wanted, OfferJson json) {
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
	public void shouldMemberOfferVariousArticlesToOwner() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), trade);
		ArticleEntity alpha = articleRandom.createPersistedEntity(ownerTradeMemberhip);
		ArticleEntity beta = articleRandom.createPersistedEntity(ownerTradeMemberhip);
		
		// Create member's articles (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		MembershipEntity memberTradeMemberhip = membershipRandom.createPersistedEntity(memberController.authenticationProvider.getAuthentication().getUser(), trade, MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.createPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = articleRandom.createPersistedEntity(memberTradeMemberhip);
		ArticleEntity cuba = articleRandom.createPersistedEntity(memberTradeMemberhip);

		// Owner offers Alpha for Australia
		OfferJson alphaForAustralia = OfferRandom.createJson(alpha.getArticleId(), australia.getArticleId());
		alphaForAustralia = fixture.post(ownerTradeMemberhip.getMembershipId(), alphaForAustralia);
		assertOffer(alpha, australia, alphaForAustralia);
		
		// Owner offers Alpha for Cuba
		OfferJson alphaForCuba = OfferRandom.createJson(alpha.getArticleId(), cuba.getArticleId());
		alphaForCuba = fixture.post(ownerTradeMemberhip.getMembershipId(), alphaForCuba);
		assertOffer(alpha, cuba, alphaForCuba);

		// Member offers Beta for Brazil
		OfferJson betaForBrazil = OfferRandom.createJson(beta.getArticleId(), brazil.getArticleId());
		betaForBrazil = fixture.post(ownerTradeMemberhip.getMembershipId(), betaForBrazil);
		assertOffer(beta, brazil, betaForBrazil);

		// Member offers Australia for Alpha
		OfferJson australiaForAlpha = OfferRandom.createJson(australia.getArticleId(), alpha.getArticleId());
		australiaForAlpha = memberController.post(memberTradeMemberhip.getMembershipId(), australiaForAlpha);
		assertOffer(australia, alpha, australiaForAlpha);
		
		// Member offers Cuba for Beta
		OfferJson cubaForAlpha = OfferRandom.createJson(cuba.getArticleId(), alpha.getArticleId());
		cubaForAlpha = memberController.post(memberTradeMemberhip.getMembershipId(), cubaForAlpha);
		assertOffer(cuba, alpha, cubaForAlpha);
	}
	
	@Test(expected=RestException.class)
	public void shouldNotCreateOfferWhenArticleDoesNotExist() {
		MembershipEntity ownerMembership = membershipRandom.createPersistedEntity(userRandom.createPersistedEntity());
		ArticleEntity alpha = articleRandom.createPersistedEntity(ownerMembership);
		OfferJson request = OfferRandom.createJson(alpha.getArticleId(), 99999999);
		try {
			fixture.post(ownerMembership.getMembershipId(), request);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertTrue(e.getMessage().contains("Offer.offeredArticleId and Offer.wantedArticleId must belong to existing Articles."));
			throw e;
		}
	}

	@Test(expected=RestException.class)
	public void shouldNotCreateOfferWhenArticlesAreNotAssociatedToTheSameTrade() {
		UserEntity owner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.createPersistedEntity(owner);
		MembershipEntity ownerMembership = membershipRandom.createPersistedEntity(owner, trade);
		ArticleEntity ownerArticle = articleRandom.createPersistedEntity(ownerMembership);

		UserEntity member = userRandom.createPersistedEntity();
		MembershipEntity memberMembership = membershipRandom.createPersistedEntity(member, tradeRandom.createPersistedEntity(member), MembershipEntity.Type.MEMBER);
		ArticleEntity memberArticle = articleRandom.createPersistedEntity(memberMembership);

		OfferJson request = OfferRandom.createJson(ownerArticle.getArticleId(), memberArticle.getArticleId());
		try {
			fixture.post(ownerMembership.getMembershipId(), request);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertTrue(e.getMessage().contains("must be associated to the same Trade"));
			throw e;
		}
	}
	
	@Test(expected=RestException.class)
	public void shouldNotCreateOfferWhenOfferingArticleDoesNotBelongToTheOfferingUser() {
		UserEntity owner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.createPersistedEntity(owner);
		MembershipEntity ownerMembership = membershipRandom.createPersistedEntity(owner, trade);
		ArticleEntity ownerArticle = articleRandom.createPersistedEntity(ownerMembership);

		UserEntity member = userRandom.createPersistedEntity();
		MembershipEntity memberMembership = membershipRandom.createPersistedEntity(member, trade, MembershipEntity.Type.MEMBER);
		ArticleEntity memberArticle = articleRandom.createPersistedEntity(memberMembership);

		OfferJson request = OfferRandom.createJson(memberArticle.getArticleId(), ownerArticle.getArticleId());
		try {
			fixture.post(ownerMembership.getMembershipId(), request);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertTrue(e.getMessage().contains("Offer.offeredArticleId must belong to the offering User.userId."));
			throw e;
		}
	}

	@Test(expected=RestException.class)
	public void shouldNotCreateTradeWhenMembershipDoesNotBelongToAuthenticatedUser() {
		// Create a trade for a random user
		TradeEntity trade = tradeRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), trade);
		ArticleEntity alpha = articleRandom.createPersistedEntity(ownerTradeMemberhip);
		
		// Create member's articles (country names)
		MembershipEntity memberTradeMemberhip = membershipRandom.createPersistedEntity(userRandom.createPersistedEntity(), trade, MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.createPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		OfferJson alphaForAustralia = OfferRandom.createJson(alpha.getArticleId(), australia.getArticleId());
		alphaForAustralia = fixture.post(ownerTradeMemberhip.getMembershipId(), alphaForAustralia);
		
		// Member offers Australia for Alpha
		OfferJson australiaForAlpha = OfferRandom.createJson(australia.getArticleId(), alpha.getArticleId());
		try {
			australiaForAlpha = fixture.post(memberTradeMemberhip.getMembershipId(), australiaForAlpha);
		} catch (RestException e) {
			assertTrue(e.getMessage().contains("Membership must belong to the current authenticated User"));
			throw e;
		}
	}

	@Test
	public void shouldOwnerOfferArticleToMember() {
		UserEntity owner = fixture.authenticationProvider.getAuthentication().getUser();
		TradeEntity trade = tradeRandom.createPersistedEntity(owner);
		MembershipEntity ownerMembership = membershipRandom.createPersistedEntity(owner, trade);
		ArticleEntity ownerArticle = articleRandom.createPersistedEntity(ownerMembership);

		UserEntity member = userRandom.createPersistedEntity();
		MembershipEntity memberMembership = membershipRandom.createPersistedEntity(member, trade, MembershipEntity.Type.MEMBER);
		ArticleEntity memberArticle = articleRandom.createPersistedEntity(memberMembership);

		OfferJson request = OfferRandom.createJson(ownerArticle.getArticleId(), memberArticle.getArticleId());
		OfferJson response = fixture.post(ownerMembership.getMembershipId(), request);
		assertNotNull(response);
		assertNotNull(response.getOfferId());
	}
	
}
