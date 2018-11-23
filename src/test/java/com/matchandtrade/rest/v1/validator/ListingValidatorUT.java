package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ListingService;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.v1.json.ListingJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListingValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	private ListingValidator fixture = new ListingValidator();
	@Mock
	private ListingService listingService;

	@Before
	public void before() {
		mockArticleRespositoryFacade();
		mockListingService();
	}

	private void mockArticleRespositoryFacade() {
		doReturn(new ArticleEntity()).when(articleRepositoryFacadeMock).find(1);
		doReturn(new ArticleEntity()).when(articleRepositoryFacadeMock).findByUserIdAndArticleId(1, 1);
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;
	}

	private void mockListingService() {
		doReturn(new SearchResult<MembershipEntity>(new ArrayList<>(), new Pagination(1, 1)))
			.when(listingService).findMembershipByUserIdAndMembershpiId(any(), any());

		List<MembershipEntity> resultList = new ArrayList<>();
		resultList.add(new MembershipEntity());
		SearchResult<MembershipEntity> searchResult = new SearchResult<>(resultList, new Pagination(1, 1, 1L));
		doReturn(searchResult)
			.when(listingService).findMembershipByUserIdAndMembershpiId(1, 1);
		fixture.listingService = listingService;
	}

	@Test
	public void validatePost_When_MembershipAndArticleBelongToAuthenticatedUser_Then_Succeeds() {
		ListingJson listing = new ListingJson(1,1);
		fixture.validatePost(1, listing);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipIdIsNull_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(null,1);
			fixture.validatePost(1,listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("HTTP Status 400 Bad Request: Listing.membershipId cannot be null", e.getMessage());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_ArticleIdIsNull_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(1,null);
			fixture.validatePost(1,listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("HTTP Status 400 Bad Request: Listing.articleId cannot be null", e.getMessage());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_ArticleDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(1,0);
			fixture.validatePost(1,listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(0,1);
			fixture.validatePost(1,listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_ArticleDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(1,0);
			fixture.validateDelete(1,listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_MembershipDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			ListingJson listing = new ListingJson(0, 1);
			fixture.validateDelete(1, listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
