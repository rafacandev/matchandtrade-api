package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipArticleControllerPostIT {

	private MembershipArticleController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipRandom membershipRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleController(true);
		}
	}

	@Test
	public void shouldCreateArticle() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson request = ArticleRandom.nextJson();
		request.setName("MembershipArticleControllerPostIT.shouldCreateName.name");
		request.setDescription("MembershipArticleControllerPostIT.shouldCreateName.description");
		ArticleJson response = fixture.post(existingTradeMemberhip.getMembershipId(), request);
		
		assertNotNull(response.getArticleId());
		assertEquals(request.getName(), response.getName());
		assertEquals(request.getDescription(), response.getDescription());
	}
	
	@Test(expected = RestException.class)
	public void postCannotHaveDuplicatedName() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson article = ArticleRandom.nextJson();
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
	}
	
	@Test(expected=RestException.class)
	public void postNameIsNull() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson article = ArticleRandom.nextJson();
		article.setName(null);
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
	}

	@Test(expected = RestException.class)
	public void postNameTooLong() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson article = ArticleRandom.nextJson();
		article.setName("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
	}

	@Test(expected = RestException.class)
	public void shouldNotAllowDescriptionGreaterThan500Characters() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson article = ArticleRandom.nextJson();
		article.setName("12345678901234567890");
		article.setDescription("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
				+ "500 Characters above.");
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
	}
	
	@Test(expected = RestException.class)
	public void postNameTooShort() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson article = ArticleRandom.nextJson();
		article.setName("ab");
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
	}

	@Test(expected = RestException.class)
	public void postMembershipNotFound() {
		ArticleJson article = ArticleRandom.nextJson();
		fixture.post(-1, article);
	}

	@Test(expected=RestException.class)
	public void postUserNotAssociatedWithMembership() {
		MembershipEntity existingTradeMemberhip = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleJson article = ArticleRandom.nextJson();
		fixture = mockControllerFactory.getArticleController(false);
		fixture.post(existingTradeMemberhip.getMembershipId(), article);
	}
	
}
