package com.matchandtrade.persistence.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class TradeQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		name("trade.name"), tradeId("trade.tradeId");

		private String alias;
		
		Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
	}
	
	private static final String BASIC_HQL = "FROM TradeEntity trade";
    @Autowired
    private EntityManager entityManager;

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return QueryBuilderUtil.parameterizeQuery(searchCriteria.getCriteria(), hql, entityManager);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder(BASIC_HQL);
		hql.append(QueryBuilderUtil.buildOrderBy(searchCriteria.getOrders()));
		return QueryBuilderUtil.parameterizeQuery(searchCriteria.getCriteria(), hql, entityManager);
	}
	
}
