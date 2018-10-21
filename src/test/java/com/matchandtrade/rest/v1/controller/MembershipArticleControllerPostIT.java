package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.UserRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class MembershipArticleControllerPostIT {

	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private MembershipRandom membershipRandom;
	private MembershipArticleController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private UserRandom userRandom;

	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getMembershipArticleController(false);
		}
	}

	@Test
	public void post_When_ArticleAndMembershipBelongToAuthenticatedUser_Then_Succeeds() {
		ArticleEntity article = articleRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		MembershipEntity membership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		fixture.post(membership.getMembershipId(), article.getArticleId());
	}

	@Test(expected = RestException.class)
	public void post_When_MembershipDoesNotBelongToAuthenticatedUser_Then_ThrowRestExceptionBadRequest() {
		UserEntity user = userRandom.nextPersistedEntity();
		ArticleEntity article = articleRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		MembershipEntity membership = membershipRandom.nextPersistedEntity(user);
		try {
			fixture.post(membership.getMembershipId(), article.getArticleId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void post_When_ArticleDoesNotBelongToAuthenticatedUser_Then_ThrowRestExceptionBadRequest() {
		UserEntity user = userRandom.nextPersistedEntity();
		ArticleEntity article = articleRandom.nextPersistedEntity(user, false);
		MembershipEntity membership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		try {
			fixture.post(membership.getMembershipId(), article.getArticleId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
