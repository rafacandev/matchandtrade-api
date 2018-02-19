package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.criteria.OfferQueryBuilder;
import com.matchandtrade.persistence.criteria.QueryBuilder;
import com.matchandtrade.persistence.criteria.TradeMembershipQueryBuilder;
import com.matchandtrade.persistence.criteria.TradeQueryBuilder;
import com.matchandtrade.persistence.criteria.UserQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.QueryableRepository;

@Component
public class SearchService {

	@Autowired
	private QueryableRepository<ItemEntity> queryableItem;
	@Autowired
	private QueryableRepository<OfferEntity> queryableOffer;
	@Autowired
	private QueryableRepository<TradeEntity> queryableTrade;
	@Autowired
	private QueryableRepository<TradeMembershipEntity> queryableTradeMembership;
	@Autowired
	private QueryableRepository<UserEntity> queryableUser;
	@Autowired
	private OfferQueryBuilder offerQueryBuilder;
	@Autowired
	private ItemQueryBuilder itemQueryBuilder;
	@Autowired
	private TradeQueryBuilder tradeQueryBuilder;
	@Autowired
	private TradeMembershipQueryBuilder tradeMembershipQueryBuilder;
	@Autowired
	private UserQueryBuilder userQueryBuilder;
	
	
	@SuppressWarnings("unchecked")
	public <T> SearchResult<T> search(SearchCriteria searchCriteria, Class<? extends QueryBuilder> queryBuilderClass) {
		if (ItemQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableItem.query(searchCriteria, itemQueryBuilder);
		}
		if (OfferQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableOffer.query(searchCriteria, offerQueryBuilder);
		}
		if (TradeQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableTrade.query(searchCriteria, tradeQueryBuilder);
		}
		if (TradeMembershipQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableTradeMembership.query(searchCriteria, tradeMembershipQueryBuilder);
		}
		if (UserQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableUser.query(searchCriteria, userQueryBuilder);
		}
		return null;
	}

}
