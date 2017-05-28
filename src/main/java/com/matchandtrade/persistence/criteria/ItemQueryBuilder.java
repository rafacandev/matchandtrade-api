package com.matchandtrade.persistence.criteria;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;

@Component
public class ItemQueryBuilder implements QueryBuilder {

	public enum Criterion {
		itemIdIsNot, tradeMembershipId, name, tradeId
	}
	
    @Autowired
    private SessionFactory sessionFactory;
    
    private static final String BASIC_HQL = "FROM TradeMembershipEntity AS tm"
    		+ " INNER JOIN tm.trade AS trade"
    		+ " INNER JOIN tm.items AS item"
    		+ " WHERE 1=1 ";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) " + BASIC_HQL);
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder(BASIC_HQL);
		return parameterizeQuery(searchCriteria, hql);
	}

	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		// Add Criterion
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				hql.append(" AND tm.tradeMembershipId = :tradeMembershipId");
			}
			if (c.getField().equals(Criterion.name)) {
				hql.append(" AND UPPER(item.name) LIKE UPPER(:name)");
			}
			if (c.getField().equals(Criterion.itemIdIsNot)) {
				hql.append(" AND item.itemId != :itemIdIsNot");
			}
			if (c.getField().equals(Criterion.tradeId)) {
				hql.append(" AND trade.tradeId = :tradeId");
			}
		}
		Query result = sessionFactory.getCurrentSession().createQuery(hql.toString());
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				result.setParameter("tradeMembershipId", c.getValue());
			}
			if (c.getField().equals(Criterion.name)) {
				result.setParameter("name", "%"+c.getValue()+"%");
			}
			if (c.getField().equals(Criterion.itemIdIsNot)) {
				result.setParameter("itemIdIsNot", c.getValue());
			}
			if (c.getField().equals(Criterion.tradeId)) {
				result.setParameter("tradeId", c.getValue());
			}
		}
		return result;
	}

}
