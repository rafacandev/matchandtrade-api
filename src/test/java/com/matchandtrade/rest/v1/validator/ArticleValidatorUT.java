package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.StringRandom;
import com.matchandtrade.test.random.UserRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleValidatorUT {

	@Autowired
	private ArticleRandom articleRandom;
	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	private ArticleJson defaultArticle;
	private ArticleValidator fixture;
	private UserRandom userRandom;
	@Mock
	private UserRepositoryFacade userRepositoryFacadeMock;

	@Before
	public void before() {
		fixture = new ArticleValidator();
		when(userRepositoryFacadeMock.get(1)).thenReturn(new UserEntity());
		fixture.userRepositoryFacade = userRepositoryFacadeMock;
		when(articleRepositoryFacadeMock.get(1)).thenReturn(new ArticleEntity());
		when(articleRepositoryFacadeMock.getByUserIdAndArticleId(1, 1)).thenReturn(new ArticleEntity());
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;
		defaultArticle = new ArticleJson();
		defaultArticle.setName("name");
		defaultArticle.setDescription("description");
	}

	@Test
	public void validatePost_When_NameLengthIs150_Expects_Success() {
		ArticleJson article = new ArticleJson();
		article.setName(StringRandom.sequencialNumericString(150));
		fixture.validatePost(1, article);
	}

	@Test
	public void validatePost_When_WithNameLengthIs3_Expects_Success() {
		ArticleJson article = new ArticleJson();
		article.setName(StringRandom.sequencialNumericString(3));
		fixture.validatePost(1, article);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameIsNull_Expects_BadRequest() {
		ArticleJson article = new ArticleJson();
		article.setDescription("description");
		try {
			fixture.validatePost(1, article);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameLengthIs2_Expects_BadRequest() {
		ArticleJson article = new ArticleJson();
		article.setName(StringRandom.sequencialNumericString(2));
		try {
			fixture.validatePost(1, article);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_When_NameLengthIs151_Expects_BadRequest() {
		ArticleJson article = new ArticleJson();
		article.setName(StringRandom.sequencialNumericString(151));
		try {
			fixture.validatePost(1, article);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw (e);
		}
	}

	@Test
	public void validatePost_When_DescriptionLengthIs2000_Expects_Success() {
		defaultArticle.setDescription(StringRandom.sequencialNumericString(2000));
		fixture.validatePost(1, defaultArticle);
	}

	@Test
	public void validatePost_When_DescriptionIsNull_Expects_Success() {
		fixture.validatePost(1, defaultArticle);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_DescriptionLengthIs2001_Expects_Success() {
		defaultArticle.setDescription(StringRandom.sequencialNumericString(2001));
		try {
			fixture.validatePost(1, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePost_WhenUserDoesNotExist_Expects_BadRequest() {
		when(userRepositoryFacadeMock.get(0)).thenReturn(null);
		try {
			fixture.validatePost(0, defaultArticle);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validateGet_When_ArticleIdExist_Expects_Success() {
		fixture.validateGet(1);
	}

	@Test(expected = RestException.class)
	public void validateGet_When_ArticleIdExists_Expects_NotFound() {
		try {
			fixture.validateGet(0);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePut_When_ArticleIdExists_Expects_Success() {
		defaultArticle.setArticleId(1);
		fixture.validatePut(1, defaultArticle);
	}

	@Test(expected = RestException.class)
	public void validatePut_When_ArticleIdDoesNotExists_Expects_BadRequest() {
		ArticleEntity article = articleRandom.nextPersistedEntity();
		ArticleJson expected = ArticleTransformer.transform(article);
		try {
			fixture.validatePut(1, expected);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_UserDoesNotExist_Expects_BadRequest() {
		ArticleEntity article = articleRandom.nextPersistedEntity();
		ArticleJson expected = ArticleTransformer.transform(article);
		try {
			fixture.validatePut(0, expected);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
