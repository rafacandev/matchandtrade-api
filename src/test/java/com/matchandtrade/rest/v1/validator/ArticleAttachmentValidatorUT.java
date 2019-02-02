package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.test.helper.ArticleHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(MockitoJUnitRunner.class)
public class ArticleAttachmentValidatorUT {
	private ArticleAttachmentValidator fixture = new ArticleAttachmentValidator();
	@Mock
	private ArticleService mockedArticleService;
	@Mock
	private AttachmentService mockedAttachmentService;
	private ArticleEntity existingArticle;
	private AttachmentEntity existingAttachment;

	@Before
	public void before() {
		existingArticle = new ArticleEntity();
		existingArticle.setArticleId(1);

		existingAttachment = new AttachmentEntity();
		existingAttachment.setAttachmentId(UUID.randomUUID());

		when(mockedArticleService.findByArticleId(existingArticle.getArticleId())).thenReturn(existingArticle);
		fixture.articleService = mockedArticleService;
		when(mockedAttachmentService.findByAttachmentId(existingAttachment.getAttachmentId())).thenReturn(existingAttachment);
		fixture.attachmentService = mockedAttachmentService;
	}

	@Test(expected = RestException.class)
	public void validatePut_When_ArticleDoesNotExist_Then_NotFound() {
		try {
			fixture.validatePut(-1, UUID.randomUUID());
		} catch (RestException e) {
			verifyThatArticleIsNotFound(e);
			return;
		}
	}

	@Test(expected = RestException.class)
	public void validatePut_When_AttachmentDoesNotExist_Then_NotFound() {
		try {
			fixture.validatePut(existingArticle.getArticleId(), UUID.randomUUID());
		} catch (RestException e) {
			assertEquals(NOT_FOUND, e.getHttpStatus());
			assertEquals("Attachment.attachmentId was not found", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validatePut_When_ArticleAndAttachmentExist_Then_Succeeds() {
		fixture.validatePut(existingArticle.getArticleId(), existingAttachment.getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void validateGet_When_ArticleDoesNotExist_Then_NotFound() {
		try {
			fixture.validateGet(-1);
		} catch (RestException e) {
			verifyThatArticleIsNotFound(e);
		}
	}

	private void verifyThatArticleIsNotFound(RestException e) {
		assertEquals(NOT_FOUND, e.getHttpStatus());
		assertEquals("Article.articleId was not found", e.getDescription());
		throw e;
	}
}
