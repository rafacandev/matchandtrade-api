package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.WantItemRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.WantItemJson;

@Component
public class WantItemValidator {

	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
	@Autowired
	private WantItemRepositoryFacade wantItemRepository;
	
	/**
	 * <p>
	 * Check if {@code desiredItemId} belongs to another {@code TradeMembership} within the same {@code Trade}.
	 * This will avoid to want an item of a different trade, or to want an item that belongs to the same user (TradeMembership).
	 * </p>
	 * @param tradeMembershipId
	 * @param desiredItemId
	 */
	private void checkIfItemBelongsToAnotherTradeMembershipWithinTheSameTrade(Integer tradeMembershipId, Integer desiredItemId) {
		TradeMembershipEntity tradeMembership = tradeMembershipRepository.get(tradeMembershipId);
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(1,1));
		searchCriteria.addCriterion(TradeMembershipQueryBuilder.Criterion.tradeId, tradeMembership.getTrade().getTradeId());
		searchCriteria.addCriterion(TradeMembershipQueryBuilder.Criterion.tradeMembershipId, tradeMembershipId, Restriction.NOT_EQUALS);
		searchCriteria.addCriterion(TradeMembershipQueryBuilder.Criterion.itemId, desiredItemId);
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipRepository.query(searchCriteria);
		if (searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "WantItem.item must belong to another TradeMembership within the same Trade.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.item} already exists for the Item.
	 * @param itemId
	 * @param desiredItemId
	 */
	private void checkIfItemIsUnique(Integer itemId, Integer desiredItemId) {
		int count = wantItemRepository.countItemWantItem(itemId, desiredItemId);
		if (count > 0) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Item.wantItem.item must be unique within the same Item.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.priority} already exists for the Item.
	 * @param itemId
	 * @param priority
	 */
	private void checkIfItemPriorityExists(Integer itemId, Integer priority) {
		int count = wantItemRepository.countItemWantItemPriority(itemId, priority);
		if (count > 0) {
			throw new RestException(HttpStatus.BAD_REQUEST, "WantItem.priority must be unique within the same Item.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.priority} is given and its value must be between 1 and 1000.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.item} is given and its value must be between 1 and 1000.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.item.itemId} is given and its value must be between 1 and 1000.
	 * @param request
	 */
	private void checkRequest(final WantItemJson request) {
		if (request.getPriority() == null || request.getPriority() < 0 || request.getPriority() > 1000) {
			throw new RestException(HttpStatus.BAD_REQUEST, "WantItem.priority is mandatory and its value must be between 1 and 1000");
		}
		if (request.getItem() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "WantItem.item is mandatory");
		}
		if (request.getItem().getItemId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "WantItem.item.itemId is mandatory");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.priority} is given and its value must be between 1 and 1000.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.item} is given and its value must be between 1 and 1000.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.item.itemId} is given and its value must be between 1 and 1000.
	 * 
	 * <p>
	 * Check if {@code desiredItemId} belongs to another {@code TradeMembership} within the same {@code Trade}.
	 * This will avoid to want an item of a different trade, or to want an item that belongs to the same user (TradeMembership).
	 * </p>
	 * 
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.priority} already exists for the Item.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code WantItem.priority} already exists for the Item.
	 * 
	 * @param tradeMembershipId
	 * @param itemId
	 * @param request
	 */
	public void validatePost(final Integer tradeMembershipId, final Integer itemId, final WantItemJson request) {
		checkRequest(request);
		checkIfItemBelongsToAnotherTradeMembershipWithinTheSameTrade(tradeMembershipId, request.getItem().getItemId());
		checkIfItemPriorityExists(itemId, request.getPriority());
		checkIfItemIsUnique(itemId, request.getItem().getItemId());
	}

}
