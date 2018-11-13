package com.matchandtrade.rest.v1.controller;

import static org.junit.Assert.assertEquals;

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
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.test.TestingDefaultAnnotations;
import com.matchandtrade.test.random.AttachmentRandom;
import com.matchandtrade.test.random.ArticleRandom;
import com.matchandtrade.test.random.MembershipRandom;
import com.matchandtrade.test.random.UserRandom;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class ArticleAttachmentControllerDeleteIT {

	@Autowired
	private ArticleRandom articleRandom;
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade fileRepositoryFacade;
	private AttachmentEntity existingAttachment;
	@Autowired
	private AttachmentRandom attachmentRandom;
	private ArticleAttachmentController fixture;
	@Autowired
	private MockControllerFactory mockControllerFactory;
	@Autowired
	private MembershipRandom membershipRandom;
	@Autowired
	private UserRandom userRandom;
	
	@Before
	public void before() throws IOException {
		if (fixture == null) {
			fixture = mockControllerFactory.getArticleFileController();
		}
		existingAttachment = attachmentRandom.createPersistedEntity();
	}

	@Test
	public void delete_When_DeletingAttachmentFromArticle_Expects_Sucess() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		article = articleRandom.createAttachmentToArticle(article, "delete-test.png");
		fixture.delete(article.getArticleId(), article.getAttachments().iterator().next().getAttachmentId());
		SearchResult<AttachmentEntity> files = fileRepositoryFacade.findAttachmentsByArticleId(article.getArticleId(), 1, 10);
		assertEquals(0, files.getResultList().size());
		assertEquals(0, files.getPagination().getTotal());
	}

	@Test(expected = RestException.class)
	public void delete_When_DeletingAttachmentThatBelongsToDifferentUser_Expects_BadRequest() {
		ArticleEntity article = articleRandom.createPersistedEntity();
		article.getAttachments().add(existingAttachment);
		articleRepositoryFacade.save(article);
		try {
			fixture.delete(article.getArticleId(), existingAttachment.getAttachmentId());
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}

	@Test(expected = RestException.class)
	public void delete_When_NonExistingAttachment_Expects_BadRequest() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		try{
			fixture.delete(article.getArticleId(), -1);
		} catch (RestException e) {
			assertEquals(HttpStatus.BAD_REQUEST, e.getHttpStatus());
			throw e;
		}
	}


	@Test
	public void delete_When_UserOwnsArticleAndAttachmentExists_Then_Succeeds() {
		ArticleEntity article = articleRandom.createPersistedEntity(fixture.authenticationProvider.getAuthentication().getUser());
		article = articleRandom.createAttachmentToArticle(article, "delete-test.png");
		fixture.delete(article.getArticleId(), article.getAttachments().iterator().next().getAttachmentId());
	}

}
