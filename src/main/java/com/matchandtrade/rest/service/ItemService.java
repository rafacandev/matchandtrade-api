package com.matchandtrade.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
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
	public SearchResult<ItemEntity> getAll(Integer tradeMembershipId) {
		// TODO This is incorrect. We need to transform in a proper search criteria
		TradeMembershipEntity tm = tradeMembershipRepository.get(tradeMembershipId);
		List<ItemEntity> items = new ArrayList<>();
		items.addAll(tm.getItems());
		return new SearchResult<>(items, new Pagination(1,1));
	}
	
}
