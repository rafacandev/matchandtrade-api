package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
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

	@Transactional
	public void create(Integer articleId, UUID attachmentId) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		AttachmentEntity attachment = attachmentService.findByAttachmentId(attachmentId);
		article.getAttachments().add(attachment);
		articleService.update(article);
	}
}
