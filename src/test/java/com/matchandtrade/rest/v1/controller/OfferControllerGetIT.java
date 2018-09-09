package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerGetIT {
	
	private OfferController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private MembershipRandom membershipRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getOfferController(true);
		}
	}
	
	@Test
	public void shouldGetByOfferId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's articles (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		MembershipEntity memberTradeMemberhip = membershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), australia.getArticleId());

		OfferJson response = fixture.get(ownerTradeMemberhip.getMembershipId(), alphaForAustralia.getOfferId());
		assertEquals(alphaForAustralia.getOfferId(), response.getOfferId());
		assertEquals(alphaForAustralia.getOfferedArticle().getArticleId(), response.getOfferedArticleId());
		assertEquals(alphaForAustralia.getWantedArticle().getArticleId(), response.getWantedArticleId());
	}

	@Test
	public void shouldGetAll() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's articles (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		MembershipEntity memberTradeMemberhip = membershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity canada = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), australia.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), canada.getArticleId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getMembershipId(), null, null, 1, 10);
		assertEquals(3, response.getPagination().getTotal());
		assertEquals(alphaForAustralia.getOfferId(), response.getResultList().get(0).getOfferId());
	}
	
	@Test
	public void shouldGetByOfferedArticleId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		ArticleEntity beta = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's articles (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		MembershipEntity memberTradeMemberhip = membershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity canada = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), australia.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), beta.getArticleId(), canada.getArticleId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), null, 1, 10);
		assertEquals(2, response.getPagination().getTotal());
	}

	@Test
	public void shouldGetByWantedArticleId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		ArticleEntity beta = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's articles (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		MembershipEntity memberTradeMemberhip = membershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		ArticleEntity australia = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity canada = articleRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), australia.getArticleId());
		OfferEntity offer = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), alpha.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getMembershipId(), beta.getArticleId(), canada.getArticleId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getMembershipId(), null, brazil.getArticleId(), 1, 10);
		assertEquals(1, response.getPagination().getTotal());
		assertEquals(offer.getOfferId(), response.getResultList().get(0).getOfferId());
	}


}
