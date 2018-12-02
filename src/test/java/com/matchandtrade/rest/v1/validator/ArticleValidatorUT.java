package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.StringRandom;
import com.matchandtrade.test.helper.SearchHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleValidatorUT {

	@Mock
	private ArticleService articleService;
	private ArticleTransformer articleTransformer = new ArticleTransformer();
	private ArticleEntity existingArticleOwnedByDifferentUser;
	private ArticleEntity existingListedArticle;
	private UserEntity existingUser;
	private ArticleJson givenExistingArticle;
	private ArticleValidator fixture;
	@Mock
	private MembershipService mockMembershipService;
	@Mock
	private UserRepositoryFacade mockUserRepositoryFacade;

	@Before
	public void before() {
		fixture = new ArticleValidator();
		existingUser = new UserEntity();
		existingUser.setUserId(1);

		givenExistingArticle = new ArticleJson();
		givenExistingArticle.setArticleId(20);
		givenExistingArticle.setName("existing-article-name");
		ArticleEntity existingArticle = articleTransformer.transform(givenExistingArticle);

		existingListedArticle = new ArticleEntity();
		existingListedArticle.setArticleId(21);

		existingArticleOwnedByDifferentUser = new ArticleEntity();
		existingArticleOwnedByDifferentUser.setArticleId(22);

		when(mockUserRepositoryFacade.findByUserId(existingUser.getUserId())).thenReturn(existingUser);
		fixture.userRepositoryFacade = mockUserRepositoryFacade;

		when(articleService.findByArticleId(givenExistingArticle.getArticleId())).thenReturn(existingArticle);
		when(articleService.findByUserIdAndArticleId(existingUser.getUserId(), existingArticle.getArticleId())).thenReturn(existingArticle);
		when(articleService.findByUserIdAndArticleId(existingUser.getUserId(), existingListedArticle.getArticleId())).thenReturn(existingListedArticle);
		fixture.articleService = articleService;

		MembershipEntity existingListedMembership = new MembershipEntity();
		existingListedMembership.setMembershipId(30);
		existingListedMembership.getArticles().add(existingListedArticle);

		when(mockMembershipService.findByArticleIdId(existingListedArticle.getArticleId(), 1, 10))
			.thenReturn(SearchHelper.buildSearchResult(existingListedMembership));
		when(mockMembershipService.findByArticleIdId(givenExistingArticle.getArticleId(), 1, 10))
			.thenReturn(SearchHelper.buildEmptySearchResult());
		fixture.membershipService = mockMembershipService;
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_ArticleIsListed_Then_Forbidden() {
		try {
			fixture.validateDelete(existingUser.getUserId(), existingListedArticle.getArticleId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			assertEquals("Article.articleId: 21 is listed on Membership.membershipId: [30]", e.getDescription());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnArticle_Then_Forbidden() {
		try {
			fixture.validateDelete(existingUser.getUserId(), existingArticleOwnedByDifferentUser.getArticleId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			assertEquals("User.userId: 1 does not own Article.articleId: 22", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validateDelete_When_ArticleIsNotListed_Then_Succeeds() {
		fixture.validateDelete(existingUser.getUserId(), givenExistingArticle.getArticleId());
	}

	@Test
	public void validateGet_When_ArticleExist_Then_Succeeds() {
		fixture.validateGet(givenExistingArticle.getArticleId());
	}

	@Test(expected = RestException.class)
	public void validateGet_When_ArticleDoesNotExist_Then_NotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_NameLengthIs150_Then_Succeeds() {
		givenExistingArticle.setName(StringRandom.sequentialNumericString(150));
		fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
	}

	@Test
	public void validatePost_When_NameLengthIs3_Then_Succeeds() {
		givenExistingArticle.setName(StringRandom.sequentialNumericString(3));
		fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameIsNull_Then_BadRequest() {
		givenExistingArticle.setName(null);
		try {
			fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameLengthIs2_Then_BadRequest() {
		givenExistingArticle.setName(StringRandom.sequentialNumericString(2));
		try {
			fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameLengthIs151_Then_BadRequest() {
		givenExistingArticle.setName(StringRandom.sequentialNumericString(151));
		try {
			fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test
	public void validatePost_When_DescriptionLengthIs2000_Then_Succeeds() {
		givenExistingArticle.setDescription(StringRandom.sequentialNumericString(2000));
		fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
	}

	@Test
	public void validatePost_When_DescriptionIsNull_Then_Succeeds() {
		fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_DescriptionLengthIs2001_Then_BadRequest() {
		givenExistingArticle.setDescription(StringRandom.sequentialNumericString(2001));
		try {
			fixture.validatePost(existingUser.getUserId(), givenExistingArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotExist_Then_BadRequest() {
		try {
			fixture.validatePost(0, givenExistingArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePut_When_ArticleExists_Then_Succeeds() {
		fixture.validatePut(existingUser.getUserId(), givenExistingArticle);
	}

	@Test(expected = RestException.class)
	public void validatePut_When_ArticleDoesNotExists_Then_BadRequest() {
		ArticleJson articleOwnedByDifferentUser =  articleTransformer.transform(existingArticleOwnedByDifferentUser);
		try {
			fixture.validatePut(existingUser.getUserId(), articleOwnedByDifferentUser);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_UserDoesNotExist_Then_BadRequest() {
		try {
			fixture.validatePut(0, givenExistingArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
