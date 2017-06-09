package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilderJavax;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.repository.ItemRepository;

@Repository
public class ItemRepositoryFacade {
	
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private QueryableRepositoryJavax<ItemEntity> queryableRepositoryJavax;
	@Autowired
	private ItemQueryBuilderJavax queryBuilderJavax;

	public ItemEntity get(Integer itemId) {
		return itemRepository.findOne(itemId);
	}

	public SearchResult<ItemEntity> query(SearchCriteria searchCriteria) {
		return queryableRepositoryJavax.query(searchCriteria, queryBuilderJavax);
	}
	
	public void save(ItemEntity entity) {
		itemRepository.save(entity);
	}
	
}
