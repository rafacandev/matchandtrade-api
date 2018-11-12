package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.MembershipRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleAttachmentControllerPostIT {
	
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;
	private AttachmentEntity existingAttachment;
	@Autowired
	private AttachmentRandom fileRandom;
	private ArticleAttachmentController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipRandom membershipRandom;
	private MockMultipartFile multipartFileMock;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleFileController(false);
		}
		existingAttachment = fileRandom.createPersistedEntity();
		multipartFileMock = AttachmentRandom.newSampleMockMultiPartFile();
	}

	@Test
	public void post_When_AddingAttachmentForExistingArticle_Expects_Success() {
		MembershipEntity membership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity article = articleRandom.createPersistedEntity(membership);
		AttachmentJson response = fixture.post(article.getArticleId(), existingAttachment.getAttachmentId());
		assertNotNull(response);
		assertEquals(existingAttachment.getAttachmentId(), response.getAttachmentId());
		SearchResult<AttachmentEntity> files = attachmentRepositoryFacade.findAttachmentsByArticleId(article.getArticleId(), 1, 10);
		assertEquals(1, files.getResultList().size());
		assertEquals(existingAttachment.getAttachmentId(), files.getResultList().get(0).getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void post_When_AddingMoreThan3FilesToArticle_Expects_BadRequest() {
		MembershipEntity membership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity article = articleRandom.createPersistedEntity(membership);
		article.getAttachments().add(fileRandom.createPersistedEntity());
		article.getAttachments().add(fileRandom.createPersistedEntity());
		article.getAttachments().add(fileRandom.createPersistedEntity());
		articleRepositoryFacade.save(article);
		try {
			fixture.post(article.getArticleId(), existingAttachment.getAttachmentId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test
	public void post_When_UserOwnsArticleAndCreatesAttachment_Then_Succeeds() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		AttachmentJson response = fixture.post(article.getArticleId(), multipartFileMock);
		assertNotNull(response);
		assertNotNull(response.getAttachmentId());
		assertEquals("image/jpeg", response.getContentType());
	}


	@Test
	public void post_When_UserOwnsArticleAndArticleAlreadyHasTwoAttachments_Then_Succeeds() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		articleRandom.createAttachmentToArticle(article, "first.png");
		articleRandom.createAttachmentToArticle(article, "second.png");
		fixture.post(article.getArticleId(), multipartFileMock);
	}

	@Test(expected = RestException.class)
	public void post_When_UserOwnsArticleAndArticleAlreadyHasThreeAttachments_Then_BadRequest() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		articleRandom.createAttachmentToArticle(article, "first.png");
		articleRandom.createAttachmentToArticle(article, "second.png");
		articleRandom.createAttachmentToArticle(article, "third.png");
		fixture.post(article.getArticleId(), multipartFileMock);
	}

}
