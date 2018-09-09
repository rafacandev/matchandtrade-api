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
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ArticleService;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.v1.json.ArticleJson;

@Component
public class ArticleValidator {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private SearchService searchService;


	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.description} length is greater than 500.
	 * @param name
	 */
	private void verifyDescriptionLength(String description) {
		if (description != null && description.length() > 500) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.description cannot be greater than 500 characters in length.");
		}
	}
	
	private void verifyThatArticleExists(Integer articleId) {
		ArticleEntity article = articleService.get(articleId);
		if (article == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "There is no Article for the given Article.articleId");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a the owner of {@code membershipId}
	 * @param membershipId
	 * @param userId
	 */
	private void verifyThatMembershipBelongsToUser(Integer membershipId, Integer userId) {
		MembershipEntity membership = membershipService.get(membershipId);
		if (!membership.getUser().getUserId().equals(userId)) {
			throw new RestException(HttpStatus.FORBIDDEN, "User.userId is not the owner of Membership.membershipId");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code membershipId} returns no Membership
	 * @param membershipId
	 * @param membershipEntity
	 */
	private void verifyMembershipTruthy(Integer membershipId, MembershipEntity membershipEntity) {
		if (membershipEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "There is no TradeMembeship.membershipId: " + membershipId);
		}
	}
	
	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipEntity.getTradeId()}
	 * @param userId
	 * @param membershipEntity
	 */
	private void verifyThatUserIsAssociatedToTrade(Integer userId, MembershipEntity membershipEntity) {
		Integer tradeId = membershipEntity.getTrade().getTradeId();
		SearchCriteria criteria = new SearchCriteria(new Pagination(1, 10));
		criteria.addCriterion(MembershipQueryBuilder.Field.tradeId, tradeId);
		criteria.addCriterion(MembershipQueryBuilder.Field.userId, userId);
		SearchResult<MembershipEntity> searchResult = searchService.search(criteria, MembershipQueryBuilder.class);
		if (searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Authenticated user is not associated with Trade.tradeId: " + tradeId);
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipId}
	 * @param userId
	 * @param membershipId
	 * @param membershipEntity
	 */
	private void verifyThatUserIsAssociatedToMembership(Integer userId, Integer membershipId, MembershipEntity membershipEntity) {
		if (!userId.equals(membershipEntity.getUser().getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not associated with Membership.membershipId: " + membershipId);
		}
	}
	
	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 * @param name
	 */
	private void verifyName(String name) {
		if (name == null || name.length() < 3 || name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name is mandatory and must be between 3 and 150 characters in length.");
		}
	}
	
	/**
	 * An article can be deleted only by their owners. See {@code validateOwndership()}
	 * Article must exist.
	 * @param membershipId
	 * @param articleId
	 */
	public void validateDelete(Integer membershipId, Integer userId, Integer articleId) {
		validateOwnership(userId, membershipId);
		verifyThatArticleExists(articleId);
	}

	public void validateGet(Integer userId, Integer membershipId) {
		validateGet(userId, membershipId, null, null);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code membershipId} returns no Membership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipId}
	 * @param userId
	 * @param membershipId
	 */
	public void validateGet(Integer userId, Integer membershipId, Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
		MembershipEntity membershipEntity = membershipService.get(membershipId);
		verifyMembershipTruthy(membershipId, membershipEntity);
		verifyThatUserIsAssociatedToTrade(userId, membershipEntity);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code membershipId} returns no Membership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipId}
	 * @param membership
	 * @param articleId
	 */
	public void validateOwnership(Integer userId, Integer membershipId) {
		MembershipEntity membershipEntity = membershipService.get(membershipId);
		verifyMembershipTruthy(membershipId, membershipEntity);
		verifyThatUserIsAssociatedToMembership(userId, membershipId, membershipEntity);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code membershipId} returns no Membership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is not unique (case insensitive) within a Membership. 
	 * @param userId
	 * @param membershipId
	 * @param article
	 */
	@Transactional
	public void validatePost(Integer userId, Integer membershipId, ArticleJson article) {
		verifyName(article.getName());
		verifyDescriptionLength(article.getDescription());
		validateOwnership(userId, membershipId);
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.membershipId, membershipId);
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.name, article.getName(), Restriction.EQUALS_IGNORE_CASE);
		SearchResult<ArticleEntity> searchResult = searchService.search(searchCriteria, ArticleQueryBuilder.class);
		if(!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name must be unique (case insensitive) within a Membership.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code membershipId} returns no Membership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code membershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is null or length is not between 3 and 150.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is not unique (case insensitive) within a Membership. 
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Article.name} is not unique (case insensitive) within a Membership. 
	 * Class {@code validatePost()}
	 * @param userId
	 * @param membershipId
	 * @param articleId
	 * @param article
	 */
	@Transactional
	public void validatePut(Integer userId, Integer membershipId, Integer articleId, ArticleJson article) {
		MembershipEntity membershipEntity = membershipService.get(membershipId);
		verifyMembershipTruthy(membershipId, membershipEntity);
		verifyThatMembershipBelongsToUser(membershipId, userId);
		verifyName(article.getName());
		verifyDescriptionLength(article.getDescription());

		ArticleEntity articleEntity = articleService.get(articleId);
		if (articleEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Did not find resource for the given Article.articleId");
		}
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.membershipId, membershipId);
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.name, article.getName(), Restriction.LIKE_IGNORE_CASE);
		// Required to check if is not the same articleId because to guarantee PUT idempotency
		searchCriteria.addCriterion(ArticleQueryBuilder.Field.articleId, article.getArticleId(), Restriction.NOT_EQUALS);
		SearchResult<ArticleEntity> searchResult = searchService.search(searchCriteria, ArticleQueryBuilder.class);
		if(!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Article.name must be unique (case insensitive) within a Membership.");
		}
	}

}
