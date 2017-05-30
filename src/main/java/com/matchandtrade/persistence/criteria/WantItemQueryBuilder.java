package com.matchandtrade.persistence.criteria;

import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.buildClause;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class WantItemQueryBuilder implements QueryBuilder {

	public enum Criterion {
		itemId, priority
	}
	
    @Autowired
    private SessionFactory sessionFactory;
    
    private static final String BASIC_HQL = " FROM WantItemEntity AS wantItem"
    		+ " INNER JOIN wantItem.item AS item";

    @Override
    public Query buildCountQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder("SELECT COUNT(*)" + BASIC_HQL);
    	return parameterizeQuery(searchCriteria, hql);
    }

    @Override
	public Query buildSearchQuery(SearchCriteria searchCriteria) {
    	StringBuilder hql = new StringBuilder(BASIC_HQL);
		return parameterizeQuery(searchCriteria, hql);
	}

	private Query parameterizeQuery(SearchCriteria searchCriteria, StringBuilder hql) {
		hql.append(" WHERE 1=1");
		
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.itemId)) {
				hql.append(buildClause("item.itemId", "itemId", c));
			}
			if (c.getField().equals(Criterion.priority)) {
				hql.append(buildClause("wantItem.priority", "priority", c));
			}
		}
		Query result = sessionFactory.getCurrentSession().createQuery(hql.toString());
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.itemId)) {
				result.setParameter("itemId", c.getValue());
			}
			if (c.getField().equals(Criterion.priority)) {
				result.setParameter("priority", c.getValue());
			}
		}
		return result;
	}

}
