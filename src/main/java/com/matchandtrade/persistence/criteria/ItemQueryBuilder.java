package com.matchandtrade.persistence.criteria;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;

@Component
public class ItemQueryBuilder implements QueryBuilder {

	public enum Criterion {
		itemIdIsNot, tradeMembershipId, name
	}
	
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM TradeMembershipEntity AS tm INNER JOIN tm.items AS item WHERE 1=1 ");
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("FROM TradeMembershipEntity AS tm INNER JOIN tm.items AS item WHERE 1=1 ");
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
		}
		return result;
	}

}
