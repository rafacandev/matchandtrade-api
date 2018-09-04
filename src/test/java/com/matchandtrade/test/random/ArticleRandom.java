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
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.transformer.ArticleTransformer;

@Component
public class ArticleRandom {
	
	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
	@Autowired
	private TradeMembershipRandom tradeMembershipRandom;
	@Autowired
	private UserRepository userRepository;
	

	public static ArticleEntity nextEntity() {
		return ArticleTransformer.transform(nextJson());
	}
	
	public static ArticleJson nextJson() {
		ArticleJson result = new ArticleJson();
		result.setName(StringRandom.nextName());
		return result;
	}
	
	@Transactional
	public ArticleEntity nextPersistedEntity(TradeMembershipEntity tradeMembership) {
		TradeMembershipEntity tme = tradeMembershipRepository.get(tradeMembership.getTradeMembershipId());
		ArticleEntity result = nextEntity();
		articleRepositoryFacade.save(result);
		tme.getArticles().add(result);
		tradeMembershipRepository.save(tme);
		return result;
	}

	@Transactional
	public ArticleEntity nextPersistedEntity(TradeMembershipEntity tradeMembership, String name) {
		TradeMembershipEntity tme = tradeMembershipRepository.get(tradeMembership.getTradeMembershipId());
		ArticleEntity result = new ArticleEntity();
		result.setName(name);
		articleRepositoryFacade.save(result);
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
		articleRepositoryFacade.save(result);
		user.getArticles().add(result);
		userRepository.save(user);
		return result;
	}

}