package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class ArticleAttachmentValidator {
	@Autowired
	ArticleService articleService;
	@Autowired
	AttachmentService attachmentService;

	private void verifyThatArticleExists(Integer articleId) {
		ArticleEntity article = articleService.findByArticleId(articleId);
		if (article == null) {
			throw new RestException(NOT_FOUND, "Article.articleId was not found");
		}
	}

	private void verifyThatArticleHasLessThanThreeAttachments(Integer articleId) {
		SearchResult searchResult = attachmentService.findByArticleId(articleId);
		if (searchResult.getPagination().getTotal() >= 3) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Articles cannot have more than 3 Attachments");
		}
	}

	public void validateGet(Integer articleId) {
		verifyThatArticleExists(articleId);
	}

	public void validatePost(Integer articleId) {
		verifyThatArticleExists(articleId);
		verifyThatArticleHasLessThanThreeAttachments(articleId);
	}
}
