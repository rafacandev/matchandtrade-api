package com.matchandtrade.persistence.criteria;

import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.buildClause;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class TradeMembershipQueryBuilderJavax implements QueryBuilderJavax {

	public enum Criterion {
		userId, tradeId, tradeMembershipId, type, itemId
	}
	
    @Autowired
    private EntityManager entityManager;

    private static final String BASIC_HQL = "FROM TradeMembershipEntity AS tradeMembership";
    
    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
		StringBuilder hql = new StringBuilder("SELECT tradeMembership " + BASIC_HQL);
		return parameterizeQuery(searchCriteria, hql);
	}
	
	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		
		boolean isTradeJoinRequired = false;
		boolean isUserJoinRequired = false;
		boolean isItemsJoinRequired = false;
		for(com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.itemId)) {
				isItemsJoinRequired = true;
			}
			if (c.getField().equals(Criterion.tradeId)) {
				isTradeJoinRequired = true;
			}
			if (c.getField().equals(Criterion.userId)) {
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
		
		hql.append(" WHERE 1=1");
		
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				hql.append(buildClause("tradeMembership.tradeMembershipId", "tradeMembershipId", c));
			}
			if (c.getField().equals(Criterion.itemId)) {
				hql.append(buildClause("item.itemId", "itemId", c));
			}
			if (c.getField().equals(Criterion.userId)) {
				hql.append(buildClause("user.userId", "userId", c));
			}
			if (c.getField().equals(Criterion.tradeId)) {
				hql.append(buildClause("trade.tradeId", "tradeId", c));
			}
			if (c.getField().equals(Criterion.type)) {
				hql.append(buildClause("tradeMembership.type", "type", c));
			}
		}
		
		Query result = entityManager.createQuery(hql.toString());
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				result.setParameter("tradeMembershipId", c.getValue());
			}
			if (c.getField().equals(Criterion.itemId)) {
				result.setParameter("itemId", c.getValue());
			}
			if (c.getField().equals(Criterion.userId)) {
				result.setParameter("userId", c.getValue());
			}
			if (c.getField().equals(Criterion.tradeId)) {
				result.setParameter("tradeId", c.getValue());
			}
			if (c.getField().equals(Criterion.type)) {
				result.setParameter("type", c.getValue());
			}
		}
		return result;
	}
	
}
