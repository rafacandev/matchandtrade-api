package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleAttachmentServiceIT {

	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private ArticleAttachmentService fixture;
	private ArticleEntity existingArticle;

	@Before
	public void before() {
		// Setup only once for better performance
		if (existingArticle == null) {
			existingArticle = articleHelper.createPersistedEntity();
		}
	}

	@Test
	public void create_When_ArticleExistsAndAttachmentIsPNG_Then_SucceedsWithThumbnail() {
		MockMultipartFile multipartFile = AttachmentHelper.newMockMultiPartFileImage(MediaType.IMAGE_PNG_VALUE);
		AttachmentEntity actual = fixture.create(existingArticle.getArticleId(), multipartFile);
		assertNotNull(actual);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getEssences());
		assertEquals(2, actual.getEssences().size());
		assertEquals("image-landscape.png", actual.getName());
		assertEquals(MediaType.IMAGE_PNG_VALUE, actual.getContentType());
	}

	@Test
	public void create_When_ArticleExistsAndAttachmentIsTxt_Then_SucceedsWithoutThumbnail() {
		MockMultipartFile multipartFile = AttachmentHelper.newTextMockMultiPartFile(MediaType.TEXT_PLAIN_VALUE);
		AttachmentEntity actual = fixture.create(existingArticle.getArticleId(), multipartFile);
		assertNotNull(actual);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getEssences());
		assertEquals(1, actual.getEssences().size());
		assertEquals("plain-text-multipart.txt", actual.getName());
		assertEquals(MediaType.TEXT_PLAIN_VALUE, actual.getContentType());
	}

	/**
	 * This is a common case when users rename their files or the download process
	 * loses/changes the content type. Content type is very easy to temper with.
	 */
	@Test
	public void create_When_ArticleExistsAndAttachmentIsTxtButContentTypeIsPng_Then_SucceedsWithoutThumbnail() {
		// GIVEN an existing article, a text file, and an image content type
		MockMultipartFile multipartFile = AttachmentHelper.newTextMockMultiPartFile(MediaType.IMAGE_PNG_VALUE);
		// WHEN create a new article attachment
		AttachmentEntity actual = fixture.create(existingArticle.getArticleId(), multipartFile);
		// THEN create the attachment but do not create any thumbnail
		assertNotNull(actual);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getEssences());
		assertEquals(1, actual.getEssences().size());
		assertEquals("plain-text-multipart.txt", actual.getName());
		assertEquals(MediaType.IMAGE_PNG_VALUE, actual.getContentType());
	}

	@Test
	public void create_When_ArticleExistsAndAttachmenDoesNotContainAnyContentType_Then_SucceedsWithoutThumbnail() {
		MockMultipartFile multipartFile = AttachmentHelper.newMockMultiPartFileImage(null);
		AttachmentEntity actual = fixture.create(existingArticle.getArticleId(), multipartFile);
		assertNotNull(actual);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getEssences());
		assertEquals(1, actual.getEssences().size());
		assertEquals("image-landscape.png", actual.getName());
		assertNull(actual.getContentType());
	}


}
