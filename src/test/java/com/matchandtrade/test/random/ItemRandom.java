package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.ItemRepository;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;

@Component
public class ItemRandom {
	
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;

	public static ItemEntity nextEntity() {
		return ItemTransformer.transform(nextJson());
	}
	
	public static ItemJson nextJson() {
		ItemJson result = new ItemJson();
		result.setName(StringRandom.nextName());
		return result;
	}
	
	public ItemEntity nextPersistedEntity(TradeMembershipEntity tradeMembership) {
		ItemEntity result = nextEntity();
		itemRepository.save(result);
		tradeMembership.getItems().add(result);
		tradeMembershipRepository.save(tradeMembership);
		return result;
	}
	
	public ItemEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(tradeOwner);
		return nextPersistedEntity(existingTradeMemberhip);
	}

}