package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.UserRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipArticleControllerPostIT {

	@Autowired
	private ArticleRandom articleRandom;
	private MembershipArticleController fixture;
	@Autowired
	private MembershipHelper membershipHelper;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getMembershipArticleController(false);
		}
	}

	@Test
	public void post_When_ArticleAndMembershipBelongToAuthenticatedUser_Then_Succeeds() {
		ArticleEntity article = articleRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		MembershipEntity membership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.post(membership.getMembershipId(), article.getArticleId());
		assertTrue(membershipHelper.membershipContainsArticle(membership.getMembershipId(), article.getArticleId()));
	}

}
