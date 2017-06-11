package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.common.Criterion.Restriction;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;

@Service
public class ItemService {

	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
	@Autowired
	private ItemRepositoryFacade itemRepository;

	@Transactional
	public void create(Integer tradeMembershipId, ItemEntity itemEntity) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepository.get(tradeMembershipId);
		itemRepository.save(itemEntity);
		tradeMembershipEntity.getItems().add(itemEntity);
		tradeMembershipRepository.save(tradeMembershipEntity);
	}

	@Transactional
	public ItemEntity get(Integer itemId) {
		return itemRepository.get(itemId);
	}

	@Transactional
	public SearchResult<ItemEntity> search(Integer tradeMembershipId, String name, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ItemQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		if (name != null) {
			searchCriteria.addCriterion(ItemQueryBuilder.Field.name, name, Restriction.LIKE_IGNORE_CASE);
		}
		return itemRepository.query(searchCriteria);
	}

	public void update(ItemEntity itemEntity) {
		itemRepository.save(itemEntity);
	}
	
}
