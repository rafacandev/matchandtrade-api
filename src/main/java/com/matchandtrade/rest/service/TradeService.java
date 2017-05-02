package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;

@Component
public class TradeService {

	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;

	@Transactional
	public void create(TradeEntity tradeEntity, UserEntity tradeOwner) {
		tradeRepository.save(tradeEntity);
		// Make authenticated user the owner of the trade
		TradeMembershipEntity tradeMembershipEntity = new TradeMembershipEntity();
		tradeMembershipEntity.setTrade(tradeEntity);
		tradeMembershipEntity.setUser(tradeOwner);
		tradeMembershipEntity.setType(TradeMembershipEntity.Type.OWNER);
		tradeMembershipRepository.save(tradeMembershipEntity);
	}

	public void update(TradeEntity tradeEntity) {
		tradeRepository.save(tradeEntity);
		// Make authenticated user the owner of the trade
		TradeMembershipEntity tradeMembershipEntity = new TradeMembershipEntity();
		tradeMembershipEntity.setTrade(tradeEntity);
		tradeMembershipEntity.setType(TradeMembershipEntity.Type.OWNER);
		tradeMembershipRepository.save(tradeMembershipEntity);
	}

	public void delete(Integer tradeId) {
		tradeRepository.delete(tradeId);
	}

	public SearchResult<TradeEntity> search(String name, Integer _pageNumber, Integer _pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(_pageNumber, _pageSize));
		if (name != null) {
			searchCriteria.addCriterion(TradeEntity.Field.name, name);
		}
		// Delegate to Repository layer
		return tradeRepository.search(searchCriteria);
	}

	public TradeEntity get(Integer tradeId) {
		return tradeRepository.get(tradeId);
	}
	
}
