package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ArticleJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArticleValidator {

	@Autowired
	ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	UserRepositoryFacade userRepositoryFacade;

	/**
	 * Throws {@code RestException(restExceptionStatus)} if there is no {@code Article} for the given {@code articleId}.
	 * @param articleId
	 * @param restExceptionStatus HttpStatus to be used if RestException is thrown
	 */
	private void verifyThatArticleExists(Integer articleId, HttpStatus restExceptionStatus) {
		ArticleEntity articleEntity = articleRepositoryFacade.get(articleId);
		if (articleEntity == null) {
			throw new RestException(restExceptionStatus, String.format("Article.articleId: %d does not exist.", articleId));
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.description} length is greater than 2000.
	 *
	 * @param description
	 */
	private void verifyThatDescriptionLengthIsLessThan2000(String description) {
		if (description != null && description.length() > 2000) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.description cannot be greater than 2000 characters in length.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 *
	 * @param name
	 */
	private void verifyThatNameLengthIsBetween3and150(String name) {
		if (name == null || name.length() < 3 || name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name is mandatory and must be between 3 and 150 characters in length.");
		}
	}

	/**
	 * Throw {@code RestException(HttpStatus.BAD_REQUEST)} when there is no User for the given {@code userId}
	 *
	 * @param userId
	 */
	private void verifyThatUserExists(Integer userId) {
		UserEntity user = userRepositoryFacade.get(userId);
		if (user == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("User.userId: %d does not exist.", userId));
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code User.userId} does not have the given {@code Article.articleId}
	 *
	 * @param articleId
	 */
	private void verifyThatUserHasArticle(Integer userId, Integer articleId) {
		ArticleEntity article = articleRepositoryFacade.getByUserIdAndArticleId(userId, articleId);
		if (article == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("User.userId: %d does have Article.articleId: %d.", userId, articleId));
		}
	}

	public void validateDelete(Integer userId, Integer articleId) {
		verifyThatUserHasArticle(userId, articleId);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if there is no {@code Article.articleId}
	 *
	 * @param articleId
	 */
	public void validateGet(Integer articleId) {
		verifyThatArticleExists(articleId, HttpStatus.NOT_FOUND);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code membershipId} returns no Membership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 *
	 * @param userId
	 * @param article
	 */
	@Transactional
	public void validatePost(Integer userId, ArticleJson article) {
		verifyThatNameLengthIsBetween3and150(article.getName());
		verifyThatDescriptionLengthIsLessThan2000(article.getDescription());
		verifyThatUserExists(userId);
	}

	/**
	 * Performs {@code validatePost(userId, article)}.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if there is no Article for the given {@code Article.articleId}.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if there is no User for the given {@code User.userId}.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code User.userId} does not have the given {@code Article.articleId}
	 *
	 * @param userId
	 * @param article
	 */
	@Transactional
	public void validatePut(Integer userId, ArticleJson article) {
		validatePost(userId, article);
		verifyThatArticleExists(article.getArticleId(), HttpStatus.BAD_REQUEST);
		verifyThatUserHasArticle(userId, article.getArticleId());
	}

}
