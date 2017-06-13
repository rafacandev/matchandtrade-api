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
	private TradeMembershipRepositoryFacade tradeMembershipRepositoryFacade;
	@Autowired
	private ItemRepositoryFacade itemRepositoryFacade;
	@Autowired
	private SearchService searchService;

	@Transactional
	public void create(Integer tradeMembershipId, ItemEntity itemEntity) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		itemRepositoryFacade.save(itemEntity);
		tradeMembershipEntity.getItems().add(itemEntity);
		tradeMembershipRepositoryFacade.save(tradeMembershipEntity);
	}

	@Transactional
	public ItemEntity get(Integer itemId) {
		return itemRepositoryFacade.get(itemId);
	}

	@Transactional
	public SearchResult<ItemEntity> searchByTradeMembershipIdName(Integer tradeMembershipId, String name, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ItemQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		if (name != null) {
			searchCriteria.addCriterion(ItemQueryBuilder.Field.name, name, Restriction.LIKE_IGNORE_CASE);
		}
		return searchService.search(searchCriteria, ItemQueryBuilder.class);
	}

	public void update(ItemEntity itemEntity) {
		itemRepositoryFacade.save(itemEntity);
	}
	
}
