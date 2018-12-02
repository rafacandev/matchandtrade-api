package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArticleAttachmentService {
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private ArticleService articleService;

	@Transactional
	public AttachmentEntity create(Integer articleId, MultipartFile multipartFile) {
		AttachmentEntity attachment = attachmentService.create(multipartFile);
		ArticleEntity article = articleService.findByArticleId(articleId);
		article.getAttachments().add(attachment);
		articleService.update(article);
		return attachment;
	}

	@Transactional
	public void delete(Integer articleId, Integer attachmentId) {
		AttachmentEntity attachment = attachmentService.findByAttachmentId(attachmentId);
		ArticleEntity article = articleService.findByArticleId(articleId);
		article.getAttachments().remove(attachment);
		articleService.update(article);
		attachmentService.delete(attachmentId);
	}

	public AttachmentEntity findByAttachmentId(Integer attachmentId) {
		return attachmentService.findByAttachmentId(attachmentId);
	}

	public SearchResult<AttachmentEntity> findByArticleId(Integer articleId) {
		return attachmentService.findByArticleId(articleId);
	}
}
