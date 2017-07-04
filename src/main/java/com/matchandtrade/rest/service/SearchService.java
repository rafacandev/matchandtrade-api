package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.criteria.QueryBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeQueryBuilder;
import com.matchandtrade.persistence.criteria.WantItemQueryBuilder;
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
	private QueryableRepository<TradeMembershipEntity> queryableWantItem;
	@Autowired
	private TradeQueryBuilder tradeQueryBuilder;
	@Autowired
	private TradeMembershipQueryBuilder tradeMembershipQueryBuilder;
	@Autowired
	private ItemQueryBuilder itemQueryBuilder;
	@Autowired
	private WantItemQueryBuilder wantItemQueryBuilder;
	
	
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
		if (WantItemQueryBuilder.class.equals(queryBuilderClass)) {
			result = (SearchResult<T>) queryableWantItem.query(searchCriteria, wantItemQueryBuilder);
		}
		return result;
	}

}
