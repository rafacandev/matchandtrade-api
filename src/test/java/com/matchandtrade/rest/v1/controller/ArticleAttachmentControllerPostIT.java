package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.AttachmentRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleAttachmentControllerPostIT {
	
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private AttachmentRandom attachmentRandom;
	private AttachmentEntity existingAttachment;
	private MockMultipartFile existingMultipartFile;
	private ArticleAttachmentController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;

	@Before
	public void before() {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleFileController();
		}
		existingAttachment = attachmentRandom.createPersistedEntity();
		existingMultipartFile = AttachmentRandom.newSampleMockMultiPartFile();
	}

	@Test
	public void post_When_UserOwnsArticleAndCreatesAttachment_Then_Succeeds() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		AttachmentJson response = fixture.post(article.getArticleId(), existingMultipartFile);
		assertNotNull(response);
		assertNotNull(response.getAttachmentId());
		assertEquals("image/jpeg", response.getContentType());
	}

	@Test
	public void post_When_UserOwnsArticleAndArticleAlreadyHasTwoAttachments_Then_Succeeds() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		articleRandom.createAttachmentToArticle(article, "first.png");
		articleRandom.createAttachmentToArticle(article, "second.png");
		fixture.post(article.getArticleId(), existingMultipartFile);
	}

	@Test(expected = RestException.class)
	public void post_When_UserOwnsArticleAndArticleAlreadyHasThreeAttachments_Then_BadRequest() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		articleRandom.createAttachmentToArticle(article, "first.png");
		articleRandom.createAttachmentToArticle(article, "second.png");
		articleRandom.createAttachmentToArticle(article, "third.png");
		fixture.post(article.getArticleId(), existingMultipartFile);
	}

}
