package com.matchandtrade.persistence.criteria;

import static com.matchandtrade.persistence.criteria.QueryBuilderUtil.buildClause;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;

@Component
public class AuthenticationQueryBuilder implements QueryBuilder {

	public enum Criterion {
		token, antiForgeryState
	}
	
	@Autowired
    private SessionFactory sessionFactory;

    private static final String BASIC_HQL = "FROM AuthenticationEntity authentication";
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
			if (c.getField().equals(Criterion.token)) {
				hql.append(buildClause("authentication.token", "token", c));
			}
			if (c.getField().equals(Criterion.antiForgeryState)) {
				hql.append(buildClause("authentication.antiForgeryState", "antiForgeryState", c));
			}
		}
		Query result = sessionFactory.getCurrentSession().createQuery(hql.toString());
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.token)) {
				result.setParameter("token", c.getValue());
			}
			if (c.getField().equals(Criterion.antiForgeryState)) {
				result.setParameter("antiForgeryState", c.getValue());
			}
		}
		return result;
	}
	
}
