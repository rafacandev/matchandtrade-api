package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.service.TradeMembershipService;
import com.matchandtrade.rest.v1.json.ArticleJson;

@Component
public class ArticleValidator {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private TradeMembershipService tradeMembershipService;
	@Autowired
	private SearchService searchService;


	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.description} length is greater than 500.
	 * @param name
	 */
	private void checkDescriptionLength(String description) {
		if (description != null && description.length() > 500) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.description cannot be greater than 500 characters in length.");
		}
	}
	
	private void checkIfArticleExists(Integer articleId) {
		ArticleEntity article = articleService.get(articleId);
		if (article == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "There is no Article for the given Article.articleId");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a the owner of {@code tradeMembershipId}
	 * @param tradeMembershipId
	 * @param userId
	 */
	private void checkIfTrademembershipBelongsToUser(Integer tradeMembershipId, Integer userId) {
		TradeMembershipEntity membership = tradeMembershipService.get(tradeMembershipId);
		if (!membership.getUser().getUserId().equals(userId)) {
			throw new RestException(HttpStatus.FORBIDDEN, "User.userId is not the owner of TradeMembership.tradeMembershipId");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * @param tradeMembershipId
	 * @param tradeMembershipEntity
	 */
	private void checkIfTradeMembershipFound(Integer tradeMembershipId, TradeMembershipEntity tradeMembershipEntity) {
		if (tradeMembershipEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "There is no TradeMembeship.tradeMembershipId: " + tradeMembershipId);
		}
	}
	
	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipEntity.getTradeId()}
	 * @param userId
	 * @param tradeMembershipEntity
	 */
	private void checkIfUserIsAssociatedToTrade(Integer userId, TradeMembershipEntity tradeMembershipEntity) {
		Integer tradeId = tradeMembershipEntity.getTrade().getTradeId();
		SearchCriteria criteria = new SearchCriteria(new Pagination(1, 10));
		criteria.addCriterion(TradeMembershipQueryBuilder.Field.tradeId, tradeId);
		criteria.addCriterion(TradeMembershipQueryBuilder.Field.userId, userId);
		SearchResult<TradeMembershipEntity> searchResult = searchService.search(criteria, TradeMembershipQueryBuilder.class);
		if (searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Authenticated user is not associated with Trade.tradeId: " + tradeId);
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * @param userId
	 * @param tradeMembershipId
	 * @param tradeMembershipEntity
	 */
	private void checkIfUserIsAssociatedToTradeMembership(Integer userId, Integer tradeMembershipId, TradeMembershipEntity tradeMembershipEntity) {
		if (!userId.equals(tradeMembershipEntity.getUser().getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not associated with TradeMembership.tradeMembershipId: " + tradeMembershipId);
		}
	}
	
	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 * @param name
	 */
	private void checkIfNameExistsAndHasMoreThanThreeCharacters(String name) {
		if (name == null || name.length() < 3 || name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name is mandatory and must be between 3 and 150 characters in length.");
		}
	}
	
	/**
	 * An article can be deleted only by their owners. See {@code validateOwndership()}
	 * Article must exist.
	 * @param tradeMembershipId
	 * @param articleId
	 */
	public void validateDelete(Integer tradeMembershipId, Integer userId, Integer articleId) {
		validateOwnership(userId, tradeMembershipId);
		checkIfArticleExists(articleId);
	}

	public void validateGet(Integer userId, Integer tradeMembershipId) {
		validateGet(userId, tradeMembershipId, null, null);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * @param userId
	 * @param tradeMembershipId
	 */
	public void validateGet(Integer userId, Integer tradeMembershipId, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipService.get(tradeMembershipId);
		checkIfTradeMembershipFound(tradeMembershipId, tradeMembershipEntity);
		checkIfUserIsAssociatedToTrade(userId, tradeMembershipEntity);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * @param tradeMembership
	 * @param articleId
	 */
	public void validateOwnership(Integer userId, Integer tradeMembershipId) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipService.get(tradeMembershipId);
		checkIfTradeMembershipFound(tradeMembershipId, tradeMembershipEntity);
		checkIfUserIsAssociatedToTradeMembership(userId, tradeMembershipId, tradeMembershipEntity);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is not unique (case insensitive) within a TradeMembership. 
	 * @param userId
	 * @param tradeMembershipId
	 * @param article
	 */
	@Transactional
	public void validatePost(Integer userId, Integer tradeMembershipId, ArticleJson article) {
		checkIfNameExistsAndHasMoreThanThreeCharacters(article.getName());
		checkDescriptionLength(article.getDescription());
		validateOwnership(userId, tradeMembershipId);
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.name, article.getName(), Restriction.EQUALS_IGNORE_CASE);
		SearchResult<ArticleEntity> searchResult = searchService.search(searchCriteria, ArticleQueryBuilder.class);
		if(!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name must be unique (case insensitive) within a TradeMembership.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is not unique (case insensitive) within a TradeMembership. 
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is not unique (case insensitive) within a TradeMembership. 
	 * Class {@code validatePost()}
	 * @param userId
	 * @param tradeMembershipId
	 * @param articleId
	 * @param article
	 */
	@Transactional
	public void validatePut(Integer userId, Integer tradeMembershipId, Integer articleId, ArticleJson article) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipService.get(tradeMembershipId);
		checkIfTradeMembershipFound(tradeMembershipId, tradeMembershipEntity);
		checkIfTrademembershipBelongsToUser(tradeMembershipId, userId);
		checkIfNameExistsAndHasMoreThanThreeCharacters(article.getName());
		checkDescriptionLength(article.getDescription());

		ArticleEntity articleEntity = articleService.get(articleId);
		if (articleEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Did not find resource for the given Article.articleId");
		}
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.name, article.getName(), Restriction.LIKE_IGNORE_CASE);
		// Required to check if is not the same articleId because to guarantee PUT idempotency
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.articleId, article.getArticleId(), Restriction.NOT_EQUALS);
		SearchResult<ArticleEntity> searchResult = searchService.search(searchCriteria, ArticleQueryBuilder.class);
		if(!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name must be unique (case insensitive) within a TradeMembership.");
		}
	}

}
