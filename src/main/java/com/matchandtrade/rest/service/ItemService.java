package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.ItemRepository;
import com.matchandtrade.repository.TradeMembershipRepository;

@Service
public class ItemService {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private ItemRepository itemRepository;

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
		searchCriteria.addCriterion(TradeMembershipEntity.Field.tradeMembershipId, tradeMembershipId);
		if (name != null) {
			searchCriteria.addCriterion(ItemEntity.Field.name, name);
		}
		return itemRepository.query(searchCriteria);
	}
	
}
