package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.test.random.StringRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	private ArticleJson defaultArticle;
	private ArticleValidator fixture;
	@Mock
	private UserRepositoryFacade userRepositoryFacadeMock;

	@Before
	public void before() {
		fixture = new ArticleValidator();
		when(userRepositoryFacadeMock.find(1)).thenReturn(new UserEntity());
		fixture.userRepositoryFacade = userRepositoryFacadeMock;

		when(articleRepositoryFacadeMock.find(1)).thenReturn(new ArticleEntity());
		when(articleRepositoryFacadeMock.findByUserIdAndArticleId(1, 1)).thenReturn(new ArticleEntity());
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;

		defaultArticle = new ArticleJson();
		defaultArticle.setName("name");
		defaultArticle.setDescription("description");
	}

	@Test
	public void validatePost_When_NameLengthIs150_Then_Succeeds() {
		defaultArticle.setName(StringRandom.sequentialNumericString(150));
		fixture.validatePost(1, defaultArticle);
	}

	@Test
	public void validatePost_When_WithNameLengthIs3_Then_Succeeds() {
		defaultArticle.setName(StringRandom.sequentialNumericString(3));
		fixture.validatePost(1, defaultArticle);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameIsNull_Then_ThrowsBadRequest() {
		defaultArticle.setName(null);
		try {
			fixture.validatePost(1, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameLengthIs2_Then_ThrowsBadRequest() {
		defaultArticle.setName(StringRandom.sequentialNumericString(2));
		try {
			fixture.validatePost(1, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameLengthIs151_Then_ThrowsBadRequest() {
		defaultArticle.setName(StringRandom.sequentialNumericString(151));
		try {
			fixture.validatePost(1, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test
	public void validatePost_When_DescriptionLengthIs2000_Then_Succeeds() {
		defaultArticle.setDescription(StringRandom.sequentialNumericString(2000));
		fixture.validatePost(1, defaultArticle);
	}

	@Test
	public void validatePost_When_DescriptionIsNull_Then_Succeeds() {
		fixture.validatePost(1, defaultArticle);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_DescriptionLengthIs2001_Then_ThrowsBadRequest() {
		defaultArticle.setDescription(StringRandom.sequentialNumericString(2001));
		try {
			fixture.validatePost(1, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotExist_Then_ThrowsBadRequest() {
		when(userRepositoryFacadeMock.find(0)).thenReturn(null);
		try {
			fixture.validatePost(0, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validateGet_When_ArticleIdExist_Then_Succeeds() {
		fixture.validateGet(1);
	}

	@Test(expected = RestException.class)
	public void validateGet_When_ArticleIdExists_Then_ThrowsNotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePut_When_ArticleIdExists_Then_Succeeds() {
		defaultArticle.setArticleId(1);
		fixture.validatePut(1, defaultArticle);
	}

	@Test(expected = RestException.class)
	public void validatePut_When_ArticleIdDoesNotExists_Then_ThrowsBadRequest() {
		try {
			fixture.validatePut(1, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_UserDoesNotExist_Then_ThrowsBadRequest() {
		try {
			fixture.validatePut(0, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
