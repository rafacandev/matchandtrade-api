package com.matchandtrade.rest.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeQueryBuilder;
import com.matchandtrade.persistence.criteria.WantItemQueryBuilder;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;

@Component
public class TradeService {

	@Autowired
	private TradeRepositoryFacade tradeRepository;
	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
	@Autowired
	private SearchService searchService;
	
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

	public void delete(Integer tradeId) {
		tradeRepository.delete(tradeId);
	}

	public TradeEntity get(Integer tradeId) {
		return tradeRepository.get(tradeId);
	}

	public SearchResult<TradeEntity> search(String name, Integer pageNumber, Integer pageSize) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(pageNumber, pageSize));
		if (name != null && !name.isEmpty()) {
			searchCriteria.addCriterion(TradeQueryBuilder.Field.name, name);
		}
		return searchService.search(searchCriteria, TradeQueryBuilder.class);
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

	public SearchResult<WantItemEntity> searchWantItemsByTradeId(Integer tradeId) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(1, 1000));
		return searchService.search(searchCriteria, WantItemQueryBuilder.class);
	}
	
}
