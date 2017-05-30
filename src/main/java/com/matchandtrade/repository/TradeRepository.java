package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipCriteriaBuilder;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Repository
public class TradeRepository {

	@Autowired
	private BasicRepository<TradeMembershipEntity> basicTradeMembershipRepository;
	@Autowired
	private BasicRepository<TradeEntity> basicTradeRepository;
	@Autowired
	private SearchableRepository<TradeMembershipEntity> searchableTradeMembershipResposity;
	@Autowired
	private QueryableRepository<TradeEntity> queryableTradeResposity;
	@Autowired
	private TradeQueryBuilder tradeCriteriaBuilder;
	@Autowired
	private TradeMembershipCriteriaBuilder tradeMembershipCriteriaBuilder;

	@Transactional
	public void delete(Integer tradeId) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(TradeMembershipCriteriaBuilder.Criterion.tradeId, tradeId);
		SearchResult<TradeMembershipEntity> sr = searchableTradeMembershipResposity.search(searchCriteria, tradeMembershipCriteriaBuilder);
		for (TradeMembershipEntity tm : sr.getResultList()) {
			basicTradeMembershipRepository.delete(tm);
		}
		TradeEntity t = get(tradeId);
		basicTradeRepository.delete(t);
	}

	@Transactional
	public TradeEntity get(Integer tradeId) {
		return basicTradeRepository.get(TradeEntity.class, tradeId);
	}
	
	@Transactional
	public void save(TradeEntity entity) {
		basicTradeRepository.save(entity);
	}

	@Transactional
	public SearchResult<TradeEntity> search(SearchCriteria searchCriteria) {
		return queryableTradeResposity.query(searchCriteria, tradeCriteriaBuilder);
	}
}
