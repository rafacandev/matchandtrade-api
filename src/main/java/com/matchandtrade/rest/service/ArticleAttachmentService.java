package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;

@Service
public class ArticleAttachmentService {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade attachmentRepositoryFacade;

	@Transactional
	public void addAttachmentToArticle(Integer articleId, Integer attachmentId) {
		AttachmentEntity attachment = attachmentRepositoryFacade.get(attachmentId);
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		article.getAttachments().add(attachment);
		articleRepositoryFacade.save(article);
	}

	public SearchResult<AttachmentEntity> search(Integer articleId, Integer pageNumber, Integer pageSize) {
		return attachmentRepositoryFacade.findAttachmentsByArticleId(articleId, pageNumber, pageSize);
	}

	@Transactional
	public void deleteAttachmentFromArticle(Integer articleId, Integer attachmentId) {
		AttachmentEntity attachment = attachmentRepositoryFacade.get(attachmentId);
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		if (article.getAttachments().remove(attachment)) {
			articleRepositoryFacade.save(article);
			attachmentRepositoryFacade.delete(attachmentId);
		}
	}

}
