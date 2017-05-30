package com.matchandtrade.persistence.criteria;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class UserCriteriaBuilder implements CriteriaBuilder {
	
	public enum Criterion {
			email
	}

    @Autowired
    private SessionFactory sessionFactory;

    @Override
	public Criteria buildSearchCriteria(SearchCriteria searchCriteria) {
		Criteria result = sessionFactory.getCurrentSession().createCriteria(UserEntity.class);
		// Add Criterion
		for (com.matchandtrade.persistence.common.Criterion c : searchCriteria.getCriteria()) {
			if (c.getField().equals(Criterion.email)) {
				result.add(Restrictions.eq("email", c.getValue()));
			}
		}
		return result;
	}
	
}
