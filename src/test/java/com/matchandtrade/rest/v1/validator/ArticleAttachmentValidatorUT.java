package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ArticleAttachmentValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	@Mock
	private AttachmentRepositoryFacade attachmentRepositoryFacadeMock;
	private ArticleAttachmentValidator fixture;
	@Mock
	private UserRepositoryFacade userRepositoryFacadeMock;

	@Before
	public void before() {
		fixture = new ArticleAttachmentValidator();

		// Mock userRepositoryFacadeMock
		UserEntity articleOwner = new UserEntity();
		articleOwner.setUserId(1);
		doReturn(articleOwner).when(userRepositoryFacadeMock).findByArticleId(1);
		fixture.userRepositoryFacade = userRepositoryFacadeMock;

		// Mock articleRepositoryFacadeMock
		doReturn(new ArticleEntity()).when(articleRepositoryFacadeMock).get(any());
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;

		// Mock attachmentRepositoryFacadeMock
		doReturn(new AttachmentEntity()).when(attachmentRepositoryFacadeMock).get(1);
		fixture.attachmentRespositoryFacade = attachmentRepositoryFacadeMock;
	}

	@Test
	public void validateDelete_When_UserOwnsArticleAndAttachmentExists_Then_Succeeds() {
		fixture.validateDelete(1, 1, 1);
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserOwnsArticleAndAttachmentDoesNotExist_Then_BadRequest() {
		try {
			fixture.validateDelete(1, 1, 0);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnArticle_Then_BadRequest() {
		try {
			fixture.validateDelete(0, 1, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validatePost_When_UserOwnsArticle_Then_Succeeds() {
		fixture.validatePost(1, 1);
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotOwnArticle_Then_BadRequest() {
		try {
			fixture.validatePost(0, 1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
