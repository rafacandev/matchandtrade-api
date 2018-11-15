package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.criteria.*;
import com.matchandtrade.persistence.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.facade.QueryableRepository;

import javax.persistence.EntityManager;

@Component
public class SearchService<E extends Entity> {

	@Autowired
	private QueryableRepository<E> queryableRepository;
	@Autowired
	private OfferQueryBuilder offerQueryBuilder;
	@Autowired
	private ArticleQueryBuilder articleQueryBuilder;
	@Autowired
	private TradeQueryBuilder tradeQueryBuilder;
	@Autowired
	private MembershipQueryBuilder membershipQueryBuilder;

	@Autowired
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	public SearchResult<E> search(SearchCriteria searchCriteria, Class<? extends QueryBuilder> queryBuilderClass) {
		if (ArticleQueryBuilder.class.equals(queryBuilderClass)) {
			return queryableRepository.query(searchCriteria, articleQueryBuilder);
		}
		if (OfferQueryBuilder.class.equals(queryBuilderClass)) {
			return queryableRepository.query(searchCriteria, offerQueryBuilder);
		}
		if (TradeQueryBuilder.class.equals(queryBuilderClass)) {
			return queryableRepository.query(searchCriteria, tradeQueryBuilder);
		}
		if (MembershipQueryBuilder.class.equals(queryBuilderClass)) {
			return queryableRepository.query(searchCriteria, membershipQueryBuilder);
		}
		throw new UnsupportedOperationException("The QueryBuilder class is not supported: " + queryBuilderClass);
	}

}
