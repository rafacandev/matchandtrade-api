package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.ItemRepository;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;

@Component
public class ItemRandom {
	
	@Autowired
	private ItemRepository itemRepository;

	@Transactional
	public ItemJson nextJson(TradeMembershipEntity tradeMembership) {
		ItemEntity itemEntity = new ItemEntity();
		itemEntity.setName(StringRandom.nextName());
		itemRepository.save(itemEntity);
		tradeMembership.getItems().add(itemEntity);
		ItemJson result = ItemTransformer.transform(itemEntity);
		return result;
	}
	
	public static ItemEntity nextEntity() {
		ItemEntity result = new ItemEntity();
		result.setName(StringRandom.nextName());
		return result;
	}

}