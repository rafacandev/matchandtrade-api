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
		articleId("article.articleId"),
		tradeId("trade.tradeId"), 
		membershipId("membership.membershipId"), 
		type("membership.type"),
		userId("user.userId"); 

		private String alias;

		private Field(String alias) {
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
			if (c.getField().equals(Field.articleId)) {
				isArticlesJoinRequired = true;
			}
			if (c.getField().equals(Field.tradeId)) {
				isTradeJoinRequired = true;
			}
			if (c.getField().equals(Field.userId)) {
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
