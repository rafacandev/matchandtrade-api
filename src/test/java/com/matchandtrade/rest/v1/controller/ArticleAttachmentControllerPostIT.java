package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.AttachmentJson;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleAttachmentControllerPostIT {
	
	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade fileRepositoryFacade;
	private AttachmentEntity file;
	@Autowired
	private AttachmentRandom fileRandom;
	private ArticleAttachmentController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipRandom membershipRandom;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleFileController(false);
		}
		file = fileRandom.createPersistedEntity();
	}
	
	@Test
	public void post_When_AddingAttachmentForExistingArticle_Expects_Success() {
		MembershipEntity membership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity article = articleRandom.nextPersistedEntity(membership);
		AttachmentJson response = fixture.post(article.getArticleId(), file.getAttachmentId());
		assertNotNull(response);
		assertEquals(file.getAttachmentId(), response.getAttachmentId());
		SearchResult<AttachmentEntity> files = fileRepositoryFacade.findAttachmentsByArticleId(article.getArticleId(), 1, 10);
		assertEquals(1, files.getResultList().size());
		assertEquals(file.getAttachmentId(), files.getResultList().get(0).getAttachmentId());
	}

	@Test(expected = RestException.class)
	public void post_When_AddingMoreThan3FilesToArticle_Expects_BadRequest() {
		MembershipEntity membership = membershipRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		ArticleEntity article = articleRandom.nextPersistedEntity(membership);
		article.getAttachments().add(fileRandom.createPersistedEntity());
		article.getAttachments().add(fileRandom.createPersistedEntity());
		article.getAttachments().add(fileRandom.createPersistedEntity());
		articleRepositoryFacade.save(article);
		try {
			fixture.post(article.getArticleId(), file.getAttachmentId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

}
