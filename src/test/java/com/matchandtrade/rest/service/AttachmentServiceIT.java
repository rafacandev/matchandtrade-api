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
public class AttachmentServiceIT {
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private AttachmentHelper attachmentHelper;
	@Autowired
	private AttachmentService fixture;

	@Test
	public void findByArticleId_When_ArticlesExist_Then_ReturnArticle() {
		ArticleEntity existingArticle = articleHelper.createPersistedEntity();
		AttachmentEntity existingAttachment1 = attachmentHelper.createPersistedEntity(existingArticle);
		AttachmentEntity existingAttachment2 = attachmentHelper.createPersistedEntity(existingArticle);
		AttachmentEntity existingAttachment3 = attachmentHelper.createPersistedEntity(existingArticle);
		SearchResult<AttachmentEntity> searchResult = fixture.findByArticleId(existingArticle.getArticleId());
		assertEquals(3, searchResult.getPagination().getTotal());
		assertTrue(searchResult.getResultList().contains(existingAttachment1));
		assertTrue(searchResult.getResultList().contains(existingAttachment2));
		assertTrue(searchResult.getResultList().contains(existingAttachment3));
	}
}
