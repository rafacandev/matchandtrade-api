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


@RunWith(SpringRunner.class)
@DefaultTestingConfiguration
public class ArticleAttachmentServiceIT {
	@Autowired
	private ArticleHelper articleHelper;
	@Autowired
	private AttachmentHelper attachmentHelper;
	@Autowired
	private ArticleAttachmentService fixture;

	@Test
	public void findByArticleId_When_ArticlesExist_Then_ReturnArticles() {
		ArticleEntity article = articleHelper.createPersistedEntity();
		AttachmentEntity attachment = attachmentHelper.createPersistedEntity(article);
		SearchResult<AttachmentEntity> searchResult = fixture.findByArticleId(article.getArticleId());
		assertEquals(1, searchResult.getPagination().getTotal());
		assertEquals(attachment, searchResult.getResultList().get(0));
	}
}
