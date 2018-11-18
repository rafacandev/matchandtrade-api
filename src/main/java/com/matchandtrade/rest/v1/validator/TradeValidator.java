package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.MembershipQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeQueryBuilder;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.SearchService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class TradeValidator {

	@Autowired
	SearchService<TradeEntity> searchServiceTrade;
	@Autowired
	SearchService<MembershipEntity> searchServiceMembership;
	@Autowired
	TradeService tradeService;

	@Transactional
	public void validateDelete(UserEntity user, Integer tradeId) {
		verifyThatTradeExists(tradeId);
		validateThatUserOwnsTrade(tradeId, user.getUserId());
	}

	public void validateGet(Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

	public void validateGet(Integer tradeId) {
		verifyThatTradeExists(tradeId);
	}

	/**
	 * Validates if json.name is mandatory and must be between 3 and 150 characters in length.
	 * Validates if json.name is unique.
	 * @param json
	 */
	public void validatePost(TradeJson json) {
		verifyThatUserNameIsBetween3And150(json.getName());
		verifyThatDescriptionIsBetween3And1000(json.getDescription());
		verifyThatNameIsUnique(json);
	}

	/**
	 * Validates if name is mandatory and must be between 3 and 150 characters in length.
	 * Validates if the {@code Trade.tradeId} exists otherwise return status NOT_FOUND.
	 * Validates if authenticated {@code user} is the owner of the trade
	 * Validates if name is unique but not the same which is being updated
	 * @param json
	 * @param user
	 */
	@Transactional
	public void validatePut(TradeJson json, UserEntity user) {
		verifyThatUserNameIsBetween3And150(json.getName());
		verifyThatDescriptionIsBetween3And1000(json.getDescription());
		verifyThatTradeExists(json.getTradeId());
		validateThatUserOwnsTrade(json.getTradeId(), user.getUserId());
		verifyThatNameIsUniqueExceptForTheCurrentTrade(json);
	}

	private void verifyThatDescriptionIsBetween3And1000(String description) {
		if (description != null && (description.length() < 3 || description.length() > 1000)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.description must be between 3 and 1000 in length.");
		}
	}

	// TODO: Add a test in SearchService for this scenario
	private void verifyThatNameIsUnique(TradeJson json) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(TradeQueryBuilder.Field.NAME, json.getName(), Restriction.EQUALS_IGNORE_CASE);
		SearchResult<TradeEntity> searchResult = searchServiceTrade.search(searchCriteria, TradeQueryBuilder.class);
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name must be unique.");
		}
	}

	// TODO: Add a test in SearchService for this scenario
	private void verifyThatNameIsUniqueExceptForTheCurrentTrade(TradeJson json) {
		SearchCriteria searchCriteriaUniqueName = new SearchCriteria(new Pagination(1,1));
		searchCriteriaUniqueName.addCriterion(TradeQueryBuilder.Field.NAME, json.getName());
		searchCriteriaUniqueName.addCriterion(TradeQueryBuilder.Field.TRADE_ID, json.getTradeId(), Restriction.NOT_EQUALS);
		SearchResult<TradeEntity> searchResultUniqueName = searchServiceTrade.search(searchCriteriaUniqueName, TradeQueryBuilder.class);
		if (!searchResultUniqueName.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name must be unique.");
		}
	}

	private void verifyThatTradeExists(Integer tradeId) {
		TradeEntity entity = tradeService.find(tradeId);
		if (entity == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Trade.tradeId was not found");
		}
	}

	private void verifyThatUserNameIsBetween3And150(String name) {
		if (name == null || name.length() < 3 || name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.name is mandatory and its length must be between 3 and 150 in length.");
		}
	}

	// TODO: Add a test in SearchService for this scenario
	private void validateThatUserOwnsTrade(Integer tradeId, Integer userId) {
		SearchCriteria searchCriteriaTradeOwner = new SearchCriteria(new Pagination(1,1));
		searchCriteriaTradeOwner.addCriterion(MembershipQueryBuilder.Field.TRADE_ID, tradeId);
		searchCriteriaTradeOwner.addCriterion(MembershipQueryBuilder.Field.USER_ID, userId);
		searchCriteriaTradeOwner.addCriterion(MembershipQueryBuilder.Field.TYPE, MembershipEntity.Type.OWNER);
		SearchResult<MembershipEntity> searchResultTradeOwner = searchServiceMembership.search(searchCriteriaTradeOwner, MembershipQueryBuilder.class);
		if (searchResultTradeOwner.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not the owner of Trade.tradeId: " + tradeId);
		}
	}

}
