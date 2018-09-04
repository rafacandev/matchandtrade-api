package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.TradeMembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleControllerGetIT {

	private ArticleController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldGetAllArticlesByTradeMembershipId() {
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		articleRandom.nextPersistedEntity(existingTradeMembership);
		articleRandom.nextPersistedEntity(existingTradeMembership);
		articleRandom.nextPersistedEntity(existingTradeMembership);
		SearchResult<ArticleJson> response = fixture.get(existingTradeMembership.getTradeMembershipId(), null, null);
		assertEquals(3, response.getResultList().size());
	}
	
	@Test
	public void shouldGetAllArticlesByTradeMembershipIdSortedByArticleName() {
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		String firstName = "alpha";
		String secondName = "beta";
		String thirdName = "charlie";
		articleRandom.nextPersistedEntity(existingTradeMembership, thirdName);
		articleRandom.nextPersistedEntity(existingTradeMembership, secondName);
		articleRandom.nextPersistedEntity(existingTradeMembership, firstName);
		SearchResult<ArticleJson> response = fixture.get(existingTradeMembership.getTradeMembershipId(), null, null);
		assertEquals(3, response.getResultList().size());
		assertEquals(firstName, response.getResultList().get(0).getName());
		assertEquals(secondName, response.getResultList().get(1).getName());
		assertEquals(thirdName, response.getResultList().get(2).getName());
	}

	@Test
	public void shouldGetArticleByTradeMemberhipId() {
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingTradeMembership);
		ArticleJson response = fixture.get(existingTradeMembership.getTradeMembershipId(), existingArticle.getArticleId());
		assertNotNull(response);
	}

	@Test
 	public void shouldGetArticlesWhenUserIsAssociatedWithTrade() {
		// Create owner's articles (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		// Create member's articles (country names)
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(ownerTradeMemberhip.getTrade(), fixture.authenticationProvider.getAuthentication().getUser(), TradeMembershipEntity.Type.MEMBER);
		fixture.get(memberTradeMemberhip.getTradeMembershipId(), alpha.getArticleId());
	}
	
	@Test(expected = RestException.class)
 	public void shouldNotGetArticleForInexistingTradeMembershipId() {
		TradeMembershipEntity existingTradeMembership = tradeMembershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingTradeMembership);
		fixture.get(-1, existingArticle.getArticleId());
	}

	@Test(expected=RestException.class)
	public void shouldNotGetArticlesWhenUserIsAssociatedWithTrade() {
		// Create owner's articles (Greek letters)
		TradeMembershipEntity ownerTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		// Create member's articles (country names)
		TradeMembershipEntity memberTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		fixture.get(memberTradeMemberhip.getTradeMembershipId(), alpha.getArticleId());
	}

}
