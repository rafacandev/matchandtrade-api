package com.matchandtrade.rest.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;

@Component
public class TradeService {

	@Autowired
	private TradeRepositoryFacade tradeRepository;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;

	@Transactional
	public void create(TradeEntity tradeEntity, UserEntity tradeOwner) {
		tradeEntity.setState(TradeEntity.State.SUBMITTING_ITEMS); // State is SUBMITTING_ITEMS when creating a new Trade
		tradeRepository.save(tradeEntity);
		// Make authenticated user the owner of the trade
		TradeMembershipEntity tradeMembershipEntity = new TradeMembershipEntity();
		tradeMembershipEntity.setTrade(tradeEntity);
		tradeMembershipEntity.setUser(tradeOwner);
		tradeMembershipEntity.setType(TradeMembershipEntity.Type.OWNER);
		tradeMembershipRepository.save(tradeMembershipEntity);
	}

	@Transactional
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

	public SearchResult<TradeEntity> searchByName(String name, Integer _pageNumber, Integer _pageSize) {
		Pageable pageable = PersistenceUtil.buildPageable(_pageNumber, _pageSize);
		Page<TradeEntity> page = tradeRepository.findByName(name, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}

	public TradeEntity get(Integer tradeId) {
		return tradeRepository.get(tradeId);
	}
	
}
