package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.test.DefaultTestingConfiguration;
import com.matchandtrade.test.helper.ArticleHelper;
import com.matchandtrade.test.helper.AttachmentHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleAttachmentServiceIT {
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private AttachmentHelper attachmentHelper;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ArticleAttachmentService fixture;

	@Test
	public void create_When_ArticleDoesNotExist_Then_Succeeds() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity();
		fixture.create(existingArticle.getArticleId(), existingAttachment.getAttachmentId());
		SearchResult<AttachmentEntity> searchResult = attachmentService.findByArticleId(existingArticle.getArticleId());
		assertTrue(searchResult.getResultList().contains(existingAttachment));
	}

	@Test
	public void findByArticleId_When_AttachmentExist_Then_ReturnAttachments() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity existingAttachment = attachmentHelper.createPersistedEntity(existingArticle);
		SearchResult<AttachmentEntity> searchResult = fixture.findByArticleId(existingArticle.getArticleId());
		assertEquals(1, searchResult.getPagination().getTotal());
		assertEquals(existingAttachment, searchResult.getResultList().get(0));
	}

	@Test
	public void findByArticleId_When_AttachmentDoesNotExist_Then_ReturnEmptySearchResults() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		attachmentHelper.createPersistedEntity();
		SearchResult<AttachmentEntity> searchResult = fixture.findByArticleId(existingArticle.getArticleId());
		assertEquals(0, searchResult.getPagination().getTotal());
	}
}
