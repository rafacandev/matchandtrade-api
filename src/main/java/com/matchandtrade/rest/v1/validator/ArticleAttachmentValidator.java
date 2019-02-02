package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class ArticleAttachmentValidator {
	@Autowired
	ArticleService articleService;
	@Autowired
	AttachmentService attachmentService;

	public void validatePut(Integer articleId, UUID attachmentId) {
		verifyThatArticleExists(articleId);
		AttachmentEntity attachment = attachmentService.findByAttachmentId(attachmentId);
		if (attachment == null) {
			throw new RestException(NOT_FOUND, "Attachment.attachmentId was not found");
		}
	}

	public void validateGet(Integer articleId) {
		verifyThatArticleExists(articleId);
	}

	private void verifyThatArticleExists(Integer articleId) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		if (article == null) {
			throw new RestException(NOT_FOUND, "Article.articleId was not found");
		}
	}
}
