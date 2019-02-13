package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

	@Transactional
	public AttachmentEntity create(Integer articleId, MultipartFile file) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		AttachmentEntity attachment = attachmentService.create(file);
		article.getAttachments().add(attachment);
		articleService.update(article);
		return attachment;
	}

	public SearchResult<AttachmentEntity> findByArticleId(Integer articleId) {
		return attachmentService.findByArticleId(articleId);
	}
}
