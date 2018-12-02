package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.v1.json.ArticleJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleValidator {

	@Autowired
	ArticleService articleService;
	@Autowired
	UserRepositoryFacade userRepositoryFacade;
	@Autowired
	MembershipService membershipService;

	/**
	 * Throws {@code RestException(restExceptionStatus)} if there is no {@code Article} for the given {@code articleId}.
	 * @param articleId
	 * @param restExceptionStatus HttpStatus to be used if RestException is thrown
	 */
	private void verifyThatArticleExists(Integer articleId, HttpStatus restExceptionStatus) {
		ArticleEntity articleEntity = articleService.findByArticleId(articleId);
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
		UserEntity user = userRepositoryFacade.findByUserId(userId);
		if (user == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("User.userId: %d does not exist.", userId));
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code User.userId} owns the given {@code Article.articleId}
	 *
	 * @param userId
	 * @param articleId
	 */
	private void verifyThatUserOwnsArticle(Integer userId, Integer articleId) {
		ArticleEntity article = articleService.findByUserIdAndArticleId(userId, articleId);
		if (article == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, String.format("User.userId: %d does not own Article.articleId: %d", userId, articleId));
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code User.userId} owns the given {@code Article.articleId}
	 *
	 * @param userId
	 * @param articleId
	 */
	public void validateDelete(Integer userId, Integer articleId) {
		verifyThatUserOwnsArticle(userId, articleId);
		verifyThatArticleIsNotListed(articleId);
	}

	private void verifyThatArticleIsNotListed(Integer articleId) {
		SearchResult<MembershipEntity> searchResult = membershipService.findByArticleIdId(articleId, 1, 10);
		if (!searchResult.isEmpty()) {
			List<Integer> membershipIds = searchResult.getResultList().stream().map(MembershipEntity::getMembershipId).collect(Collectors.toList());
			throw new RestException(HttpStatus.FORBIDDEN, String.format("Article.articleId: %s is listed on Membership.membershipId: %s", articleId, membershipIds));
		}
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
		verifyThatUserOwnsArticle(userId, article.getArticleId());
	}

}
