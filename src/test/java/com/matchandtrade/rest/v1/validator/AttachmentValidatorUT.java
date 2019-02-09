package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(MockitoJUnitRunner.class)
public class AttachmentValidatorUT {
	private UUID existingAttachmentId = UUID.randomUUID();
	private Integer existingArticleId = 1;
	private AttachmentValidator fixture;
	@Mock
	private AttachmentService mockedAttachmentService;
	@Mock
	private ArticleService mockedArticleService;

	@Before
	public void before() {
		fixture = new AttachmentValidator();
		ArticleEntity existingArticle = new ArticleEntity();
		existingArticle.setArticleId(existingArticleId);
		AttachmentEntity existingAttachment = new AttachmentEntity();
		existingAttachment.setAttachmentId(existingAttachmentId);

		when(mockedAttachmentService.findByAttachmentId(existingAttachmentId)).thenReturn(existingAttachment);
		when(mockedAttachmentService.findByAttachmentId(not(eq(existingAttachmentId)))).thenThrow(new RestException(NOT_FOUND, "Attachment.attachmentId was not found"));
		fixture.attachmentService = mockedAttachmentService;
		when(mockedArticleService.findByArticleId(existingArticleId)).thenReturn(existingArticle);
		when(mockedArticleService.findByArticleId(not(eq(existingArticleId)))).thenThrow(new RestException(NOT_FOUND, "Article.articleId was not found"));
		fixture.articleService = mockedArticleService;
	}

	@Test
	public void validateGet_When_AttachmentExists_Then_Succeeds() {
		fixture.validateGet(existingAttachmentId);
	}

	@Test(expected = RestException.class)
	public void validateGet_When_AttachmentDoesNotExists_Then_NotFound() {
		try {
			fixture.validateGet(UUID.randomUUID());
		} catch (RestException e) {
			verifyAttachmentNotFound(e);
		}
	}

	@Test
	public void validatePost_When_SizeIsLessThan5Mb_Then_Succeeds() {
		MockMultipartFile file = AttachmentHelper.newMockMultiPartFileImage();
		fixture.validatePost(file);
	}

	@Test
	public void validateFind_When_GivenArticleIdAndArticleExists_Then_Succeeds() {
		fixture.validateFind(existingArticleId);
	}

	@Test(expected = RestException.class)
	public void validateFind_When_GivenArticleIdAndArticleDoesNotExists_Then_NotFound() {
		try {
			fixture.validateFind(-1);
		} catch (RestException e) {
			verifyArticleNotFound(e);
		}
	}

	private void verifyArticleNotFound(RestException e) {
		assertEquals(NOT_FOUND, e.getHttpStatus());
		assertEquals("Article.articleId was not found", e.getDescription());
		throw e;
	}
	private void verifyAttachmentNotFound(RestException e) {
		assertEquals(NOT_FOUND, e.getHttpStatus());
		assertEquals("Attachment.attachmentId was not found", e.getDescription());
		throw e;
	}
}
