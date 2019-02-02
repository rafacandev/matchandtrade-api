package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
	public SearchResult<AttachmentEntity> findByArticleId(Integer articleId) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		// TODO: Create a QueryableSearch if we ever allow more than 3 attachments per article
		Pagination pagination = new Pagination(1, 3);
		pagination.setTotal(article.getAttachments().size());
		List<AttachmentEntity> attachments = new ArrayList<>(article.getAttachments());
		return new SearchResult<>(attachments, pagination);
	}
}
