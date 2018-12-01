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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleAttachmentValidatorUT {

	@Mock
	private ArticleRepositoryFacade articleRepositoryFacadeMock;
	@Mock
	private AttachmentRepositoryFacade attachmentRepositoryFacadeMock;
	private ArticleAttachmentValidator fixture;
	@Mock
	private UserRepositoryFacade userRepositoryFacadeMock;

	private UserEntity existingUser;
	private ArticleEntity existingArticle;
	private ArticleEntity existingArticleOwnByDifferentUser;
	private AttachmentEntity existingAttachment;

	@Before
	public void before() {
		fixture = new ArticleAttachmentValidator();
		existingUser = new UserEntity();
		existingUser.setUserId(1);

		existingArticle = new ArticleEntity();
		existingArticle.setArticleId(11);
		existingArticleOwnByDifferentUser = new ArticleEntity();
		existingArticleOwnByDifferentUser.setArticleId(12);

		existingAttachment = new AttachmentEntity();
		existingAttachment.setAttachmentId(21);


		doReturn(existingUser).when(userRepositoryFacadeMock).findByArticleId(existingArticle.getArticleId());
		fixture.userRepositoryFacade = userRepositoryFacadeMock;

		when(articleRepositoryFacadeMock.find(existingArticle.getArticleId())).thenReturn(existingArticle);
		fixture.articleRepositoryFacade = articleRepositoryFacadeMock;

		when(attachmentRepositoryFacadeMock.find(21)).thenReturn(existingAttachment);
		fixture.attachmentRepositoryFacade = attachmentRepositoryFacadeMock;
	}

	@Test
	public void validateDelete_When_UserOwnsArticleAndAttachmentExists_Then_Succeeds() {
		fixture.validateDelete(existingUser.getUserId(), existingArticle.getArticleId(), existingAttachment.getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserOwnsArticleAndAttachmentDoesNotExist_Then_NotFound() {
		try {
			fixture.validateDelete(existingUser.getUserId(), existingArticle.getArticleId(), -1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void validateDelete_When_UserDoesNotOwnArticle_Then_Forbidden() {
		try {
			fixture.validateDelete(existingUser.getUserId(), existingArticleOwnByDifferentUser.getArticleId(), existingAttachment.getAttachmentId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void validateGet_When_AttachmentExists_Then_Succeeds() {
		fixture.validateGet(existingAttachment.getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void validateGet_When_AttachmentDoesNotExist_Then_NotFound() {
		try {
			fixture.validateGet(-1);
		} catch (RestException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
			assertEquals("Attachment.attachmentId: -1 was not found", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validatePost_When_UserOwnsArticle_Then_Succeeds() {
		fixture.validatePost(existingUser.getUserId(), existingArticle.getArticleId());
	}

	@Test(expected = RestException.class)
	public void validatePost_When_UserDoesNotOwnArticle_Then_Forbidden() {
		try {
			fixture.validatePost(existingUser.getUserId(), existingArticleOwnByDifferentUser.getArticleId());
		} catch (RestException e) {
			assertEquals(HttpStatus.FORBIDDEN, e.getHttpStatus());
			throw e;
		}
	}

}
