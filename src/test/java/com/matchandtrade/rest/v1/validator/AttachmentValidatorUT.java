package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.AttachmentService;
import com.matchandtrade.test.helper.AttachmentHelper;
import javassist.bytecode.analysis.MultiType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(MockitoJUnitRunner.class)
public class AttachmentValidatorUT {
	private UUID existingAttachmentId = UUID.randomUUID();
	private AttachmentValidator fixture;
	@Mock
	private AttachmentService mockedAttachmentService;
	private UUID notFoundAttachmentId = UUID.randomUUID();

	@Before
	public void before() {
		fixture = new AttachmentValidator();
		AttachmentEntity existingAttachment = new AttachmentEntity();
		existingAttachment.setAttachmentId(existingAttachmentId);
		when(mockedAttachmentService.findByAttachmentId(existingAttachmentId)).thenReturn(existingAttachment);
		when(mockedAttachmentService.findByAttachmentId(notFoundAttachmentId)).thenThrow(new RestException(NOT_FOUND, "Article.articleId was not found"));
		fixture.attachmentService = mockedAttachmentService;
	}

	@Test
	public void validateGet_When_AttachmentExists_Then_Succeeds() {
		fixture.validateGet(existingAttachmentId);
	}

	@Test(expected = RestException.class)
	public void validateGet_When_AttachmentDoesNotExists_Then_NotFound() {
		try {
			fixture.validateGet(notFoundAttachmentId);
		} catch (RestException e) {
			assertEquals(NOT_FOUND, e.getHttpStatus());
			assertEquals("Article.articleId was not found", e.getDescription());
			throw e;
		}
	}

	@Test
	public void validatePost_When_SizeIsLessThan5Mb_Then_Succeeds() {
		MockMultipartFile file = AttachmentHelper.newMockMultiPartFileImage(MediaType.IMAGE_PNG_VALUE);
		fixture.validatePost(file);
	}
}
