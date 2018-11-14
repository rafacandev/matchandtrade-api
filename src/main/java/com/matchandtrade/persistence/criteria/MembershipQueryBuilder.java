package com.matchandtrade.persistence.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class MembershipQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		ARTICLE_ID("article.articleId"),
		TRADE_ID("trade.tradeId"),
		MEMBERSHIP_ID("membership.membershipId"),
		TYPE("membership.type"),
		USER_ID("user.userId");

		private String alias;

		Field(String alias) {
			this.alias = alias;
		}
		
		@Override
		public String alias() {
			return alias;
		}
		
	}
	
    @Autowired
    private EntityManager entityManager;
    private static final String BASIC_HQL = "FROM MembershipEntity AS membership";
    
    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	parameterizeHql(searchCriteria, hql);
    	return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT membership " + BASIC_HQL);
		parameterizeHql(searchCriteria, hql);
		return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager);
	}
	
	private void parameterizeHql(SearchCriteria searchCriteria, StringBuilder hql) {
		boolean isTradeJoinRequired = false;
		boolean isUserJoinRequired = false;
		boolean isArticlesJoinRequired = false;
		for(Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Field.ARTICLE_ID)) {
				isArticlesJoinRequired = true;
			}
			if (c.getField().equals(Field.TRADE_ID)) {
				isTradeJoinRequired = true;
			}
			if (c.getField().equals(Field.USER_ID)) {
				isUserJoinRequired = true;
			}
		}
		
		if (isArticlesJoinRequired) {
			hql.append(" LEFT JOIN membership.articles AS article");
		}
		if (isUserJoinRequired) {
			hql.append(" LEFT JOIN membership.user AS user");
		}
		if (isTradeJoinRequired) {
			hql.append(" LEFT JOIN membership.trade AS trade");
		}
	}
	
}
