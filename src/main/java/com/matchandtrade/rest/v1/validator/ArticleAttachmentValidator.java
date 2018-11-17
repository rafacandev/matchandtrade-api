package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.AttachmentEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.AttachmentRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArticleAttachmentValidator {

	@Autowired
	ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	AttachmentRepositoryFacade attachmentRespositoryFacade;
	@Autowired
	UserRepositoryFacade userRepositoryFacade;

	public void validateDelete(Integer userId, Integer articleId, Integer attachmentId) {
		verifyThatAttachmentExists(attachmentId);
		verifyThatUserOwnsArticle(userId, articleId);
	}

	@Transactional
	public void validatePost(Integer userId, Integer articleId) {
		verifyThatUserOwnsArticle(userId, articleId);
		verifyThatArticleHasLessThanTwoFiles(articleId);
	}

	private void verifyThatArticleHasLessThanTwoFiles(Integer articleId) {
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		if (article.getAttachments().size() > 2) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Articles cannot have more than 3 files.");
		}
	}

	private void verifyThatAttachmentExists(Integer attachmentId) {
		AttachmentEntity attachment = attachmentRespositoryFacade.find(attachmentId);
		if (attachment == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("Attachment.attachmentId: %s does not exist.", attachmentId));
		}
	}

	private void verifyThatUserOwnsArticle(Integer userId, Integer articleId) {
		UserEntity user = userRepositoryFacade.findByArticleId(articleId);
		if (user == null || !userId.equals(user.getUserId())) {
			throw new RestException(HttpStatus.BAD_REQUEST,
				String.format("User.userId: %s is not the owner of Article.articleId: %s", userId, articleId));
		}
	}

}
