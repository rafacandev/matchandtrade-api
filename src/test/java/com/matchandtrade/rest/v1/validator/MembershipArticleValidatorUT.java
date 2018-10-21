package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.test.TestingDefaultAnnotations;
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
@TestingDefaultAnnotations
public class MembershipArticleValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	private MembershipArticleValidator fixture = new MembershipArticleValidator();
	@Mock
	private SearchService searchServiceMock;

	@Before
	public void before() {
		List<Object> memberships = new ArrayList<>();
		memberships.add(new MembershipEntity());
		SearchResult<Object> searchResult = new SearchResult<>(memberships, new Pagination());
		when(searchServiceMock.search(any(), any())).thenReturn(searchResult);
		fixture.searchService = searchServiceMock;

		when(articleRepositoryFacadeMock.get(1)).thenReturn(new ArticleEntity());
		when(articleRepositoryFacadeMock.getByUserIdAndArticleId(1, 1)).thenReturn(new ArticleEntity());
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;
	}

	@Test
	public void validatePost_When_MembershipAndArticleBelongToAuthenticatedUser_Then_Succeeds() {
		fixture.validatePost(1, 1, 1);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_ArticleDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		try {
			fixture.validatePost(1,1, 0);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_MembershipDoesNotBelongToAuthenticatedUser_Then_ThrowBadRequest() {
		SearchResult<Object> searchResult = new SearchResult<>(new ArrayList<>(), new Pagination());
		when(searchServiceMock.search(any(), any())).thenReturn(searchResult);
		fixture.searchService = searchServiceMock;

		try {
			fixture.validatePost(1,0, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}


}
