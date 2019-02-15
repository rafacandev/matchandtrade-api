package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleAttachmentService;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class ArticleAttachmentValidator {
	@Autowired
	ArticleService articleService;
	@Autowired
	AttachmentService attachmentService;
	@Autowired
	ArticleAttachmentService articleAttachmentService;

	private void verifyThatArticleExists(Integer articleId) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		if (article == null) {
			throw new RestException(NOT_FOUND, "Article.articleId was not found");
		}
	}

	private void verifyThatArticleHasLessThanThreeAttachments(Integer articleId) {
		SearchResult searchResult = articleAttachmentService.findByArticleId(articleId);
		if (searchResult.getPagination().getTotal() >= 3) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Articles cannot have more than 3 Attachments");
		}
	}

	private void verifyThatAttachmentExists(UUID attachmentId) {
		AttachmentEntity attachment = attachmentService.findByAttachmentId(attachmentId);
		if (attachment == null) {
			throw new RestException(NOT_FOUND, "Attachment.attachmentId was not found");
		}
	}

	public void validateGet(Integer articleId) {
		verifyThatArticleExists(articleId);
	}

	public void validatePost(Integer articleId) {
		verifyThatArticleExists(articleId);
		verifyThatArticleHasLessThanThreeAttachments(articleId);
	}

	public void validatePut(Integer articleId, UUID attachmentId) {
		verifyThatArticleExists(articleId);
		verifyThatAttachmentExists(attachmentId);
	}
}
