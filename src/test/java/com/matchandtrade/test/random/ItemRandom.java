package com.matchandtrade.test.random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ItemRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;

@Component
public class ItemRandom {
	
	@Autowired
	private ItemRepositoryFacade itemRepository;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
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
	
	@Transactional
	public ItemEntity nextPersistedEntity(TradeMembershipEntity tradeMembership) {
		TradeMembershipEntity tme = tradeMembershipRepository.get(tradeMembership.getTradeMembershipId());
		ItemEntity result = nextEntity();
		itemRepository.save(result);
		tme.getItems().add(result);
		tradeMembershipRepository.save(tme);
		return result;
	}
	
	public ItemEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(tradeOwner);
		return nextPersistedEntity(existingTradeMemberhip);
	}

}