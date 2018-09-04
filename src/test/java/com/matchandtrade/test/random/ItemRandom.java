package com.matchandtrade.test.random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.persistence.repository.UserRepository;
import com.matchandtrade.rest.v1.json.ItemJson;
import com.matchandtrade.rest.v1.transformer.ItemTransformer;

@Component
public class ItemRandom {
	
	@Autowired
	private ArticleRepositoryFacade itemRepository;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRepository userRepository;
	

	public static ArticleEntity nextEntity() {
		return ItemTransformer.transform(nextJson());
	}
	
	public static ItemJson nextJson() {
		ItemJson result = new ItemJson();
		result.setName(StringRandom.nextName());
		return result;
	}
	
	@Transactional
	public ArticleEntity nextPersistedEntity(TradeMembershipEntity tradeMembership) {
		TradeMembershipEntity tme = tradeMembershipRepository.get(tradeMembership.getTradeMembershipId());
		ArticleEntity result = nextEntity();
		itemRepository.save(result);
		tme.getArticles().add(result);
		tradeMembershipRepository.save(tme);
		return result;
	}

	@Transactional
	public ArticleEntity nextPersistedEntity(TradeMembershipEntity tradeMembership, String name) {
		TradeMembershipEntity tme = tradeMembershipRepository.get(tradeMembership.getTradeMembershipId());
		ArticleEntity result = new ArticleEntity();
		result.setName(name);
		itemRepository.save(result);
		tme.getArticles().add(result);
		tradeMembershipRepository.save(tme);
		return result;
	}
	
	@Transactional
	public ArticleEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeMembershipEntity existingTradeMemberhip = tradeMembershipRandom.nextPersistedEntity(tradeOwner);
		return nextPersistedEntity(existingTradeMemberhip);
	}

	@Transactional
	public ArticleEntity nextPersistedEntity(UserEntity user, boolean shouldCreateTrade) {
		if (shouldCreateTrade == true) {
			return nextPersistedEntity(user);
		}
		ArticleEntity result = nextEntity();
		itemRepository.save(result);
		user.getArticles().add(result);
		userRepository.save(user);
		return result;
	}

}