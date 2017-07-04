package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.criteria.QueryBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.QueryableRepository;

@Component
public class SearchService {

	@Autowired
	private QueryableRepository<ItemEntity> queryableItem;
	@Autowired
	private QueryableRepository<TradeEntity> queryableTrade;
	@Autowired
	private QueryableRepository<TradeMembershipEntity> queryableTradeMembership;
	@Autowired
	private TradeQueryBuilder tradeQueryBuilder;
	@Autowired
	private TradeMembershipQueryBuilder tradeMembershipQueryBuilder;
	@Autowired
	private ItemQueryBuilder itemQueryBuilder;
	
	
	@SuppressWarnings("unchecked")
	public <T> SearchResult<T> search(SearchCriteria searchCriteria, Class<? extends QueryBuilder> queryBuilderClass) {
		SearchResult<T> result = null;
		if (TradeQueryBuilder.class.equals(queryBuilderClass)) {
			result = (SearchResult<T>) queryableTrade.query(searchCriteria, tradeQueryBuilder);
		}
		if (TradeMembershipQueryBuilder.class.equals(queryBuilderClass)) {
			result = (SearchResult<T>) queryableTradeMembership.query(searchCriteria, tradeMembershipQueryBuilder);
		}
		if (ItemQueryBuilder.class.equals(queryBuilderClass)) {
			result = (SearchResult<T>) queryableItem.query(searchCriteria, itemQueryBuilder);
		}
		return result;
	}

}
