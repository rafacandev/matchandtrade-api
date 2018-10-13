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
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipArticleControllerGetIT {

	private MembershipArticleController fixture;
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRandom userRandom;
	@Autowired
	private MembershipRandom membershipRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getMembershipArticleController(true);
		}
	}

	@Test
	public void shouldGetAllArticlesByMembershipId() {
		MembershipEntity existingMembership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		articleRandom.nextPersistedEntity(existingMembership);
		articleRandom.nextPersistedEntity(existingMembership);
		articleRandom.nextPersistedEntity(existingMembership);
		SearchResult<ArticleJson> response = fixture.get(existingMembership.getMembershipId(), null, null);
		assertEquals(3, response.getResultList().size());
	}
	
	@Test
	public void shouldGetAllArticlesByMembershipIdSortedByArticleName() {
		MembershipEntity existingMembership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		String firstName = "alpha";
		String secondName = "beta";
		String thirdName = "charlie";
		articleRandom.nextPersistedEntity(existingMembership, thirdName);
		articleRandom.nextPersistedEntity(existingMembership, secondName);
		articleRandom.nextPersistedEntity(existingMembership, firstName);
		SearchResult<ArticleJson> response = fixture.get(existingMembership.getMembershipId(), null, null);
		assertEquals(3, response.getResultList().size());
		assertEquals(firstName, response.getResultList().get(0).getName());
		assertEquals(secondName, response.getResultList().get(1).getName());
		assertEquals(thirdName, response.getResultList().get(2).getName());
	}

	@Test
	public void shouldGetArticleByTradeMemberhipId() {
		MembershipEntity existingMembership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingMembership);
		ArticleJson response = fixture.get(existingMembership.getMembershipId(), existingArticle.getArticleId());
		assertNotNull(response);
	}

	@Test
 	public void shouldGetArticlesWhenUserIsAssociatedWithTrade() {
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		// Create member's articles (country names)
		MembershipEntity memberTradeMemberhip = membershipRandom.nextPersistedEntity(ownerTradeMemberhip.getTrade(), fixture.authenticationProvider.getAuthentication().getUser(), MembershipEntity.Type.MEMBER);
		fixture.get(memberTradeMemberhip.getMembershipId(), alpha.getArticleId());
	}
	
	@Test(expected = RestException.class)
 	public void shouldNotGetArticleForInexistingMembershipId() {
		MembershipEntity existingMembership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity existingArticle = articleRandom.nextPersistedEntity(existingMembership);
		fixture.get(-1, existingArticle.getArticleId());
	}

	@Test(expected=RestException.class)
	public void shouldNotGetArticlesWhenUserIsAssociatedWithTrade() {
		// Create owner's articles (Greek letters)
		MembershipEntity ownerTradeMemberhip = membershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		ArticleEntity alpha = articleRandom.nextPersistedEntity(ownerTradeMemberhip);
		// Create member's articles (country names)
		MembershipEntity memberTradeMemberhip = membershipRandom.nextPersistedEntity(userRandom.nextPersistedEntity());
		fixture.get(memberTradeMemberhip.getMembershipId(), alpha.getArticleId());
	}

}
