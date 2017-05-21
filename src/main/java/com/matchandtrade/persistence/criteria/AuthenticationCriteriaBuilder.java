package com.matchandtrade.persistence.criteria;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.persistence.entity.AuthenticationEntity;

@Component
public class AuthenticationCriteriaBuilder implements CriteriaBuilder, Serializable {

	public enum Criterion {
		token, antiForgeryState
	}
	
	private static final long serialVersionUID = -6171272708318857032L;
	
	@Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(AuthenticationEntity.class);
		// Add Criterion
		for (com.matchandtrade.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.token)) {
				result.add(Restrictions.eq("token", c.getValue()));
			}
			if (c.getField().equals(AuthenticationCriteriaBuilder.Criterion.antiForgeryState)) {
				result.add(Restrictions.eq("antiForgeryState", c.getValue()));
			}
		}
		return result;
	}
	
}
