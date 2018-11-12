package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AttachmentVerificator {

	@Autowired
	ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	UserRepositoryFacade userRepositoryFacade;

	@Transactional
	public void validatePost(Integer userId, Integer articleId, MultipartFile multipartFile) {
		verifyThatUserOwnsArticle(userId, articleId);
		verifyThatArticleHasLessThanTwoFiles(articleId);
	}

	private void verifyThatUserOwnsArticle(Integer userId, Integer articleId) {
		UserEntity user = userRepositoryFacade.findByArticleId(articleId);
		if (user == null || !userId.equals(user.getUserId())) {
			throw new RestException(HttpStatus.BAD_REQUEST,
				String.format("User.userId: %s is not the owner of Article.articleId: %s", userId, articleId));
		}
	}

	private void verifyThatArticleHasLessThanTwoFiles(Integer articleId) {
		ArticleEntity article = articleRepositoryFacade.get(articleId);
		if (article.getAttachments().size() > 2) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Articles cannot have more than 3 files.");
		}
	}

}
