package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.repository.TradeMembershipRepository;

@Repository
public class TradeMembershipRepositoryFacade {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private TradeMembershipQueryBuilder tradeMembershipQueryBuilder;
	@Autowired
	private QueryableRepository<TradeMembershipEntity> queriableResposity;

	public void delete(Integer tradeMembershipId) {
		tradeMembershipRepository.delete(tradeMembershipId);
	}

	public TradeMembershipEntity get(Integer tradeMembershipId) {
		return tradeMembershipRepository.findOne(tradeMembershipId);
	}

	public void save(TradeMembershipEntity entity) {
		tradeMembershipRepository.save(entity);
	}
	
	public SearchResult<TradeMembershipEntity> query(SearchCriteria searchCriteria) {
		return queriableResposity.query(searchCriteria, tradeMembershipQueryBuilder);
	}
	
	public SearchResult<TradeMembershipEntity> findByTradeIdAndUserId(Integer tradeId, Integer userId, Pagination pagination) {
		Pageable pageable = PersistenceUtil.buildPageable(pagination);
		Page<TradeMembershipEntity> page = tradeMembershipRepository.findByTrade_TradeIdAndUser_UserId(tradeId, userId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}

}
