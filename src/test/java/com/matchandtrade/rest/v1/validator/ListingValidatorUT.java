package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ListingService;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.v1.json.ListingJson;
import com.matchandtrade.test.helper.SearchHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListingValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	private ArticleEntity existingArticle;
	private MembershipEntity existingMembership;
	private UserEntity existingUser;
	private ListingValidator fixture;
	@Mock
	private ListingService listingService;

	@Before
	public void before() {
		fixture = new ListingValidator();
		existingUser = new UserEntity();
		existingUser.setUserId(1);
		existingArticle = new ArticleEntity();
		existingArticle.setArticleId(11);
		existingMembership = new MembershipEntity();
		existingMembership.setMembershipId(21);

		when(articleRepositoryFacadeMock.findByUserIdAndArticleId(existingUser.getUserId(), existingArticle.getArticleId())).thenReturn(existingArticle);
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;

		when(listingService.findMembershipByUserIdAndMembershpiId(any(), any()))
			.thenReturn(SearchHelper.buildEmptySearchResult());
		when(listingService.findMembershipByUserIdAndMembershpiId(existingUser.getUserId(), existingMembership.getMembershipId()))
			.thenReturn(SearchHelper.buildSearchResult(new MembershipEntity()));
		fixture.listingService = listingService;
	}

	@Test
	public void validatePost_When_UserOwnsMembershipAndArticle_Then_Succeeds() {
		ListingJson listing = new ListingJson(existingMembership.getMembershipId(), existingArticle.getArticleId());
		fixture.validatePost(existingUser.getUserId(), listing);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipIdIsNull_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(null,existingArticle.getArticleId());
			fixture.validatePost(existingUser.getUserId(),listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("HTTP Status 400 Bad Request: Listing.membershipId cannot be null", e.getMessage());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_ArticleIdIsNull_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(existingMembership.getMembershipId(),null);
			fixture.validatePost(existingUser.getUserId(),listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("HTTP Status 400 Bad Request: Listing.articleId cannot be null", e.getMessage());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_ArticleDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(existingMembership.getMembershipId(),0);
			fixture.validatePost(existingUser.getUserId(),listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(0, existingArticle.getArticleId());
			fixture.validatePost(existingUser.getUserId(),listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_ArticleDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(existingMembership.getMembershipId(),0);
			fixture.validateDelete(existingUser.getUserId(),listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_MembershipDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(0, existingArticle.getArticleId());
			fixture.validateDelete(existingUser.getUserId(), listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
