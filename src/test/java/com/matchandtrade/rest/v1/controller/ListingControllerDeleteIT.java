package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.v1.json.ListingJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.helper.SearchHelper;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.ListingRandom;
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
	private MembershipEntity existingMembership;
	private ListingController fixture;
	@Autowired
	private ListingRandom listingRandom;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private SearchHelper searchHelper;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getListingController();
		}
		existingArticle = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser(), false);
		existingMembership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		listingRandom.createPersisted(existingArticle.getArticleId(), existingMembership.getMembershipId());
	}

	@Test
	public void delete_When_ArticleAndMembershipBelongToAuthenticatedUser_Then_Succeeds() {
		ListingJson request = new ListingJson(existingMembership.getMembershipId(), existingArticle.getArticleId());
		fixture.delete(request);
		assertFalse(searchHelper.membershipContainsArticle(existingMembership.getMembershipId(), existingArticle.getArticleId()));
	}

}