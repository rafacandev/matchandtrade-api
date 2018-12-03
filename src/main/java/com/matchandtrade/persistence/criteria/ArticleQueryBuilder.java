package com.matchandtrade.persistence.criteria;

import com.matchandtrade.persistence.common.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;

@Component
public class ArticleQueryBuilder implements QueryBuilder {
	@Autowired
	private QueryBuilderHelper queryBuilderHelper;

	public enum Field implements com.matchandtrade.persistence.common.Field {
		TRADE_ID("trade.tradeId");

		private String alias;

		Field(String alias) { this.alias = alias; }

		@Override
		public String alias() { return alias; }
	}

    private static final String BASIC_HQL = "FROM MembershipEntity AS membership"
    		+ " INNER JOIN membership.trade AS trade"
    		+ " INNER JOIN membership.articles AS article";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return queryBuilderHelper.buildQuery(searchCriteria, hql, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT article " + BASIC_HQL);
		return queryBuilderHelper.buildQuery(searchCriteria, hql);
	}
}
