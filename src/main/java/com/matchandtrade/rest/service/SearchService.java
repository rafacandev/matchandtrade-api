package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.QueryableRepository;

@Component
public class SearchService {

	@Autowired
	private QueryableRepository<ArticleEntity> queryableArticle;
	@Autowired
	private QueryableRepository<OfferEntity> queryableOffer;
	@Autowired
	private QueryableRepository<TradeEntity> queryableTrade;
	@Autowired
	private QueryableRepository<MembershipEntity> queryableMembership;
	@Autowired
	private QueryableRepository<UserEntity> queryableUser;
	@Autowired
	private OfferQueryBuilder offerQueryBuilder;
	@Autowired
	private ArticleQueryBuilder articleQueryBuilder;
	@Autowired
	private TradeQueryBuilder tradeQueryBuilder;
	@Autowired
	private MembershipQueryBuilder membershipQueryBuilder;

	@SuppressWarnings("unchecked")
	public <T> SearchResult<T> search(SearchCriteria searchCriteria, Class<? extends QueryBuilder> queryBuilderClass) {
		if (ArticleQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableArticle.query(searchCriteria, articleQueryBuilder);
		}
		if (OfferQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableOffer.query(searchCriteria, offerQueryBuilder);
		}
		if (TradeQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableTrade.query(searchCriteria, tradeQueryBuilder);
		}
		if (MembershipQueryBuilder.class.equals(queryBuilderClass)) {
			return (SearchResult<T>) queryableMembership.query(searchCriteria, membershipQueryBuilder);
		}
		return null;
	}

}
