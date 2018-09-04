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
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.v1.json.OfferJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ItemRandom;
import com.matchandtrade.test.random.OfferRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.TradeRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class OfferControllerGetIT {
	
	private OfferController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private ItemRandom itemRandom;
	@Autowired
	private OfferRandom offerRandom;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;

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
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);

		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), australia.getArticleId());

		OfferJson response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), alphaForAustralia.getOfferId());
		assertEquals(alphaForAustralia.getOfferId(), response.getOfferId());
		assertEquals(alphaForAustralia.getOfferedArticle().getArticleId(), response.getOfferedArticleId());
		assertEquals(alphaForAustralia.getWantedArticle().getArticleId(), response.getWantedArticleId());
	}

	@Test
	public void shouldGetAll() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity canada = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		OfferEntity alphaForAustralia = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), australia.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), canada.getArticleId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), null, null, 1, 10);
		assertEquals(3, response.getPagination().getTotal());
		assertEquals(alphaForAustralia.getOfferId(), response.getResultList().get(0).getOfferId());
	}
	
	@Test
	public void shouldGetByOfferedItemId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		ArticleEntity beta = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity canada = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), australia.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), beta.getArticleId(), canada.getArticleId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), null, 1, 10);
		assertEquals(2, response.getPagination().getTotal());
	}

	@Test
	public void shouldGetByWantedItemId() {
		// Create a trade
		TradeEntity trade = tradeRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		
		// Create owner's items (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity alpha = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		ArticleEntity beta = itemRandom.nextPersistedEntity(ownerTradeMemberhip);
		
		// Create member's items (country names)
		OfferController memberController = mockControllerFactory.getOfferController(false);
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(trade, memberController.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		ArticleEntity australia = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity brazil = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		ArticleEntity canada = itemRandom.nextPersistedEntity(memberTradeMemberhip);
		
		// Owner offers Alpha for Australia
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), australia.getArticleId());
		OfferEntity offer = offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), alpha.getArticleId(), brazil.getArticleId());
		offerRandom.nextPersistedEntity(ownerTradeMemberhip.getTradeMembershipId(), beta.getArticleId(), canada.getArticleId());
		
		SearchResult<OfferJson> response = fixture.get(ownerTradeMemberhip.getTradeMembershipId(), null, brazil.getArticleId(), 1, 10);
		assertEquals(1, response.getPagination().getTotal());
		assertEquals(offer.getOfferId(), response.getResultList().get(0).getOfferId());
	}


}
