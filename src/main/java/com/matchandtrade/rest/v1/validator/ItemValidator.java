package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.ItemRepository;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.ItemJson;

@Component
public class ItemValidator {

	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;

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
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * @param userId
	 * @param tradeMembershipId
	 * @param tradeMembershipEntity
	 */
	private void checkIfUserIsAssociatedToTradeMembership(Integer userId, Integer tradeMembershipId, TradeMembershipEntity tradeMembershipEntity) {
		if (!userId.equals(tradeMembershipEntity.getUser().getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "Authenticated user is not associated to TradeMembership.tradeMembershipId: " + tradeMembershipId);
		}
	}
	
	/**
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code json.name} is null or length is not between 3 and 150.
	 * @param name
	 */
	private void checkIfNameLength(String name) {
		if (name == null || name.length() < 3 || name.length() > 150) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Item.name is mandatory and must be between 3 and 150 characters in length.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * @param userId
	 * @param tradeMembershipId
	 * @param json
	 */
	public void validateGet(Integer userId, Integer tradeMembershipId) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepository.get(tradeMembershipId);
		checkIfTradeMembershipFound(tradeMembershipId, tradeMembershipEntity);
		checkIfUserIsAssociatedToTradeMembership(userId, tradeMembershipId, tradeMembershipEntity);
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code json.name} is null or length is not between 3 and 150.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Item.name} is not unique (case insensitive) within a TradeMembership. 
	 * @param userId
	 * @param tradeMembershipId
	 * @param json
	 */
	@Transactional
	public void validatePost(Integer userId, Integer tradeMembershipId, ItemJson json) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepository.get(tradeMembershipId);
		checkIfTradeMembershipFound(tradeMembershipId, tradeMembershipEntity);
		checkIfUserIsAssociatedToTradeMembership(userId, tradeMembershipId, tradeMembershipEntity);
		checkIfNameLength(json.getName());
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(ItemQueryBuilder.Criterion.tradeMembershipId, tradeMembershipId);
		searchCriteria.addCriterion(ItemQueryBuilder.Criterion.name, json.getName());
		SearchResult<ItemEntity> searchResult = itemRepository.query(searchCriteria);
		if(!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Item.name must be unique (case insensitive) within a TradeMembership.");
		}
	}

	/**
	 * Throws {@code RestException(HttpStatus.NOT_FOUND)} if {@code tradeMembershipId} returns no TradeMembership
	 * Throws {@code RestException(HttpStatus.FORBIDDEN)} if {@code userId} is not a associated with {@code tradeMembershipId}
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code json.name} is null or length is not between 3 and 150.
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Item.name} is not unique (case insensitive) within a TradeMembership. 
	 * Throws {@code RestException(HttpStatus.BAD_REQUEST)} if {@code Item.name} is not unique (case insensitive) within a TradeMembership. 
	 * Class {@code validatePost()}
	 * @param userId
	 * @param tradeMembershipId
	 * @param itemId
	 * @param json
	 */
	@Transactional
	public void validatePut(Integer userId, Integer tradeMembershipId, Integer itemId, ItemJson json) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepository.get(tradeMembershipId);
		checkIfTradeMembershipFound(tradeMembershipId, tradeMembershipEntity);
		checkIfUserIsAssociatedToTradeMembership(userId, tradeMembershipId, tradeMembershipEntity);
		checkIfNameLength(json.getName());

		ItemEntity itemEntity = itemRepository.get(itemId);
		if (itemEntity == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Did not find resource for the given Item.itemId");
		}
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(ItemQueryBuilder.Criterion.tradeMembershipId, tradeMembershipId);
		searchCriteria.addCriterion(ItemQueryBuilder.Criterion.name, json.getName());
		// Required to check if is not the same itemId because to guarantee PUT idempotency
		searchCriteria.addCriterion(ItemQueryBuilder.Criterion.itemIdIsNot, json.getItemId());
		SearchResult<ItemEntity> searchResult = itemRepository.query(searchCriteria);
		if(!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Item.name must be unique (case insensitive) within a TradeMembership.");
		}
		
	}

}
