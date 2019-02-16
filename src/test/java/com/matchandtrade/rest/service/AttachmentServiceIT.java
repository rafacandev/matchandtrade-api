package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class AttachmentServiceIT {
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private AttachmentHelper attachmentHelper;
	@Autowired
	private AttachmentService fixture;

	@Test
	public void create_When_IsPngFileAndContentTypeIsPng_Then_CreatePngFile() {
		MultipartFile multipartFile = AttachmentHelper.newMockMultiPartFileImage();
		AttachmentEntity actual = fixture.create(multipartFile);
		assertTrue(actual.getName().endsWith(".png"));
	}

	@Test
	public void create_When_IsPngFileAndContentTypeIsJpg_Then_CreateJpgFile() {
		MultipartFile multipartFile = AttachmentHelper.newMockMultiPartFileImage(MediaType.IMAGE_JPEG_VALUE, "image.jpg");
		AttachmentEntity actual = fixture.create(multipartFile);
		assertTrue(actual.getName().endsWith(".jpg"));
	}

	// TODO: Handle the optional in AttachmentRepositoryFacade
	@Test(expected = NoSuchElementException.class)
	public void delete_When_AttachmentExists_Then_Succeeds() {
		AttachmentEntity expected = attachmentHelper.createPersistedEntity();
		fixture.delete(expected.getAttachmentId());
		fixture.findByAttachmentId(expected.getAttachmentId());
	}

	@Test(expected = NoSuchElementException.class)
	public void delete_When_AttachmentIsAssociatedToArticle_Then_Succeeds() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity expected = attachmentHelper.createPersistedEntity(existingArticle);
		fixture.delete(expected.getAttachmentId());
		fixture.findByAttachmentId(expected.getAttachmentId());
	}
}
