package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Criterion;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.AuthenticationEntity;

@Component
public class AuthenticationCriteriaBuilder implements CriteriaBuilder {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(AuthenticationEntity.class);
		// Add Criterion
		for (Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(AuthenticationEntity.Field.token)) {
				result.add(Restrictions.eq(AuthenticationEntity.Field.token.toString(), c.getValue()));
			}
			if (c.getField().equals(AuthenticationEntity.Field.antiForgeryState)) {
				result.add(Restrictions.eq(AuthenticationEntity.Field.antiForgeryState.toString(), c.getValue()));
			}
		}
		return result;
	}
	
}
