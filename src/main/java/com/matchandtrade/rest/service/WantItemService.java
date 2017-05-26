package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.repository.ItemRepository;

@Service
public class WantItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Transactional
	public void create(WantItemEntity wantItem, Integer itemId) {
		ItemEntity item = itemRepository.get(itemId);
		item.getWantItems().add(wantItem);
		itemRepository.save(item);
	}

}
