package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleAttachmentServiceIT {

	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private ArticleAttachmentService fixture;

	@Test
	public void create_When_ArticleExistsAndAttachmentIsPNG_Then_Succeeds() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		MockMultipartFile multipartFile = AttachmentHelper.newSampleMockMultiPartFile();
		AttachmentEntity actual = fixture.create(existingArticle.getArticleId(), multipartFile);
		assertNotNull(actual);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getEssences());
		assertEquals(2, actual.getEssences().size());
		assertEquals("image-landscape.png", actual.getName());
		assertEquals(MediaType.IMAGE_PNG_VALUE, actual.getContentType());
	}

	@Test
	public void create_When_ArticleExistsAndAttachmentIsTxt_Then_Succeeds() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
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
	 * When, create a new article attachment
	 * Given, an existing article, a text file, a image content type
	 * Then, create the attachment but do not create any thumbnail
	 *
	 * This is a very common case when users rename their files or some download process
	 * loses/changes the content type. Content type is very easy to temper with.
	 */
	@Test
	public void create_When_ArticleExistsAndAttachmentIsTxtButContentTypeIsPng_Then_Succeeds() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		MockMultipartFile multipartFile = AttachmentHelper.newTextMockMultiPartFile(MediaType.IMAGE_PNG_VALUE);
		AttachmentEntity actual = fixture.create(existingArticle.getArticleId(), multipartFile);
		assertNotNull(actual);
		assertNotNull(actual.getAttachmentId());
		assertNotNull(actual.getEssences());
		assertEquals(1, actual.getEssences().size());
		assertEquals("plain-text-multipart.txt", actual.getName());
		assertEquals(MediaType.IMAGE_PNG_VALUE, actual.getContentType());
	}

}
