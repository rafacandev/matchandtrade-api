package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.repository.ItemRepository;

@Repository
public class ItemRepositoryFacade {
	
	@Autowired
	private ItemRepository itemRepository;

	public ItemEntity get(Integer itemId) {
		return itemRepository.findOne(itemId);
	}

	public void save(ItemEntity entity) {
		itemRepository.save(entity);
	}
	
}
