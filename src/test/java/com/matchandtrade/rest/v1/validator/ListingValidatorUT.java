package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListingValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	private ListingValidator fixture = new ListingValidator();
	@Mock
	private SearchService searchServiceMock;

	@Before
	public void before() {
		List<Object> memberships = new ArrayList<>();
		memberships.add(new MembershipEntity());
		SearchResult<Object> searchResult = new SearchResult<>(memberships, new Pagination(1,1, 1L));
		when(searchServiceMock.searchCake(any(), any())).thenReturn(searchResult);
		fixture.searchService = searchServiceMock;

		when(articleRepositoryFacadeMock.get(1)).thenReturn(new ArticleEntity());
		when(articleRepositoryFacadeMock.getByUserIdAndArticleId(1, 1)).thenReturn(new ArticleEntity());
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;
	}

	private void mockSearchServiceToReturnNoSearchResults() {
		SearchResult<Object> searchResult = new SearchResult<>(new ArrayList<>(), new Pagination());
		when(searchServiceMock.searchCake(any(), any())).thenReturn(searchResult);
		fixture.searchService = searchServiceMock;
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
		mockSearchServiceToReturnNoSearchResults();
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
		mockSearchServiceToReturnNoSearchResults();
		try {
			ListingJson listing = new ListingJson(0, 1);
			fixture.validateDelete(1, listing);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
