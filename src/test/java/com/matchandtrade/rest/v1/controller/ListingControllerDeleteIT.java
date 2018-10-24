package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.helper.MembershipHelper;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ListingControllerDeleteIT {

	@Autowired
	private ArticleRandom articleRandom;
	private ArticleEntity existingArticle;
	@Autowired
	private MembershipRandom membershipRandom;
	private ListingController fixture;
	private MembershipEntity existingMembership;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipHelper membershipHelper;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getListingController(false);
		}
		existingArticle = articleRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		existingMembership = membershipRandom.nextPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		existingMembership.getArticles().add(existingArticle);
		membershipRepositoryFacade.save(existingMembership);
	}

	@Test
	public void delete_When_ArticleAndMembershipBelongToAuthenticatedUser_Then_Succeeds() {
		fixture.delete(existingMembership.getMembershipId(), existingArticle.getArticleId());
		assertFalse(membershipHelper.membershipContainsArticle(existingMembership.getMembershipId(), existingArticle.getArticleId()));
	}

}