package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.rest.RestException;

@Component
public class ArticleAttachmentValidator {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private AttachmentRepositoryFacade fileRespositoryFacade;
	@Autowired
	private ArticleValidator articleValidator;
	
	public void validateDelete(Integer userId, Integer membershipId, Integer articleId, Integer fileId) {
		articleValidator.verifyThatUserHasArticle(userId, articleId);
		AttachmentEntity file = fileRespositoryFacade.get(fileId);
		if (file == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "There is no File for the given File.fileId.");
		}
	}
	
	/**
	 * Same as in {@link ArticleValidator.verifyThatUserHasArticle(userId, articleId)}.
	 * Additionally, validates if the target {@code Article} for the given {@code articleId} has less than two files.
	 * 
	 * @param userId
	 * @param membershipId
	 * @param articleId
	 */
	@Transactional
	public void validatePost(Integer userId, Integer membershipId, Integer articleId) {
		articleValidator.verifyThatUserHasArticle(userId, articleId);
		validateThatArticleHasLessThanTwoFiles(articleId);
	}

	private void validateThatArticleHasLessThanTwoFiles(Integer articleId) {
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		if (article.getAttachments().size() > 2) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Articles cannot have more than 3 files.");
		}
	}

	public void validateGet(Integer userId, Integer membershipId, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

}
