package com.matchandtrade.persistence.criteria;

import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.buildClause;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class UserQueryBuilder implements QueryBuilder {
	
	public enum Criterion {
			email
	}

    @Autowired
    private SessionFactory sessionFactory;
    private static final String BASIC_HQL = "FROM UserEntity user";

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
		hql.append(" WHERE 1=1");
		
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.email)) {
				hql.append(buildClause("user.email", "email", c));
			}
		}
		
		Query result = sessionFactory.getCurrentSession().createQuery(hql.toString());
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.email)) {
				result.setParameter("email", c.getValue());
			}
		}
		return result;
	}
	
	
}
