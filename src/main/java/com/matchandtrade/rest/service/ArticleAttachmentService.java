package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ArticleAttachmentService {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;

	@Transactional
	public void create(Integer articleId, UUID attachmentId) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		AttachmentEntity attachment = attachmentService.findByAttachmentId(attachmentId);
		article.getAttachments().add(attachment);
		articleService.update(article);
	}

	public SearchResult<AttachmentEntity> findByArticleId(Integer articleId) {
		return attachmentRepositoryFacade.findByArticleId(articleId);
	}
}
