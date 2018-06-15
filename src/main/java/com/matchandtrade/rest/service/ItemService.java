package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.common.Sort;
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
	@Autowired
	private OfferService offerService;

	@Transactional
	public void create(Integer tradeMembershipId, ItemEntity itemEntity) {
		TradeMembershipEntity tradeMembershipEntity = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		itemRepositoryFacade.save(itemEntity);
		tradeMembershipEntity.getItems().add(itemEntity);
		tradeMembershipRepositoryFacade.save(tradeMembershipEntity);
	}

	@Transactional
	public void delete(Integer tradeMembershipId, Integer itemId) {
		offerService.deleteOffersForItem(itemId);
		TradeMembershipEntity membership = tradeMembershipRepositoryFacade.get(tradeMembershipId);
		ItemEntity item = itemRepositoryFacade.get(itemId);
		membership.getItems().remove(item);
		tradeMembershipRepositoryFacade.save(membership);
		itemRepositoryFacade.delete(itemId);
	}

	public ItemEntity get(Integer itemId) {
		return itemRepositoryFacade.get(itemId);
	}
	
	public boolean exists(Integer ...itemIds) {
		return itemRepositoryFacade.exists(itemIds);
	}

	@Transactional
	public SearchResult<ItemEntity> searchByTradeMembershipId(Integer tradeMembershipId, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		searchCriteria.addCriterion(ItemQueryBuilder.Field.tradeMembershipId, tradeMembershipId);
		searchCriteria.addSort(ItemQueryBuilder.Field.name, Sort.Type.ASC);
		return searchService.search(searchCriteria, ItemQueryBuilder.class);
	}

	public void update(ItemEntity itemEntity) {
		itemRepositoryFacade.save(itemEntity);
	}

}
