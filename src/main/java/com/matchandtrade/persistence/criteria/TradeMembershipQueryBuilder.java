package com.matchandtrade.persistence.criteria;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Criterion;
import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class TradeMembershipQueryBuilder implements QueryBuilder {

	public enum Field implements com.matchandtrade.persistence.common.Field {
		articleId("item.articleId"),
		tradeId("trade.tradeId"), 
		tradeMembershipId("tradeMembership.tradeMembershipId"), 
		type("tradeMembership.type"),
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
    private static final String BASIC_HQL = "FROM TradeMembershipEntity AS tradeMembership";
    
    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	parameterizeHql(searchCriteria, hql);
    	return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager, true);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT tradeMembership " + BASIC_HQL);
		parameterizeHql(searchCriteria, hql);
		return QueryBuilderUtil.buildQuery(searchCriteria, hql, entityManager);
	}
	
	private void parameterizeHql(SearchCriteria searchCriteria, StringBuilder hql) {
		boolean isTradeJoinRequired = false;
		boolean isUserJoinRequired = false;
		boolean isItemsJoinRequired = false;
		for(Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Field.articleId)) {
				isItemsJoinRequired = true;
			}
			if (c.getField().equals(Field.tradeId)) {
				isTradeJoinRequired = true;
			}
			if (c.getField().equals(Field.userId)) {
				isUserJoinRequired = true;
			}
		}
		
		if (isItemsJoinRequired) {
			hql.append(" INNER JOIN tradeMembership.items AS item");
		}
		if (isUserJoinRequired) {
			hql.append(" INNER JOIN tradeMembership.user AS user");
		}
		if (isTradeJoinRequired) {
			hql.append(" INNER JOIN tradeMembership.trade AS trade");
		}
	}
	
}
