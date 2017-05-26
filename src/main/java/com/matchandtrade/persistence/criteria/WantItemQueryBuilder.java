package com.matchandtrade.persistence.criteria;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;

@Component
public class WantItemQueryBuilder implements QueryBuilder {

	public enum Criterion {
		tradeMembershipId, itemId, priority, WantItem_item
	}
	
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) "
    			+ "FROM "
    			+ "TradeMembershipEntity AS tm "
    			+ "INNER JOIN tm.items AS item "
    			+ "INNER JOIN item.wantItems AS wi "
    			+ "WHERE 1=1 ");
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("FROM "
    			+ "TradeMembershipEntity AS tm "
    			+ "INNER JOIN tm.items AS item "
    			+ "INNER JOIN item.wantItems AS wi "
    			+ "WHERE 1=1 ");
		return parameterizeQuery(searchCriteria, hql);
	}

	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				hql.append(" AND tm.tradeMembershipId = :tradeMembershipId");
			}
			if (c.getField().equals(Criterion.itemId)) {
				hql.append(" AND item.itemId = :itemId");
			}
			if (c.getField().equals(Criterion.priority)) {
				hql.append(" AND wi.priority = :priority");
			}
			if (c.getField().equals(Criterion.WantItem_item)) {
				hql.append(" AND wi.item.itemId = :WantItem_item");
			}
		}
		Query result = sessionFactory.getCurrentSession().createQuery(hql.toString());
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.tradeMembershipId)) {
				result.setParameter("tradeMembershipId", c.getValue());
			}
			if (c.getField().equals(Criterion.itemId)) {
				result.setParameter("itemId", c.getValue());
			}
			if (c.getField().equals(Criterion.priority)) {
				result.setParameter("priority", c.getValue());
			}
			if (c.getField().equals(Criterion.WantItem_item)) {
				result.setParameter("WantItem_item", c.getValue());
			}
		}
		return result;
	}

}
